# LiteSpring

## 实现部分：IOC，AOP
### IOC：
1.Bean的定义、存储、注册、获取
2.Cglib及Jdk实现类实例化
3.注入Object
4.资源加载器解析对象
5.实现完整Bean生命周期

### AOP：
1.基于JDK/Cglib代理实现AOP切面
2.AOP注入Bean生命周期
3.根据注解扫描注入对象
4.利用AOP给对象注入属性

## 扩展：
### 1.三级缓存实现循环依赖
```java
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    // 一级普通对象
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    // 二级缓存提前暴漏没有完全实例化的对象
    protected final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();

    // 三级缓存存放代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>();


    private final Map<String, DisposableBean> disposableBeans = new LinkedHashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (null == singletonObject) {
            singletonObject = earlySingletonObjects.get(beanName);
            // 判断二级缓存中是否有对象，这个对象就是代理对象，因为只有代理对象才会放到三级缓存中
            if (null == singletonObject) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    // 把三级缓存中的代理对象中的真实对象获取出来，放入二级缓存中
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory){
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

}
```
### 2.多重优先级，细粒度划分操作

1.定义注解
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadPriority {
    int value() default 3; //默认优先级3
    String function() default ""; //优先级2的触发函数
```

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreloadTrigger {}
```

2.AOP注解拦截
```java
@Aspect
@Component
public class Priority2PreloadAspect {
    @Autowired
    private ConfigurableListableBeanFactory beanFactory;
    @Autowired
    private PriorityBeanCollector priorityCollector;

    //存储已预加载的 Bean
    private final Set<String> preloadedBeans = ConcurrentHashMap.newKeySet();

    //拦截所有标记为触发函数的方法
    @Around("execution(* *(..)) && @annotation(PreloadTrigger)")
    public Object preloadOnFunctionCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String currentFunction = className + "." + methodName;

        //查找需要预加载的 Bean
        Map<String, String> triggerFunctionMap = priorityCollector.getBeanTriggerFunctionMap();
        triggerFunctionMap.forEach((beanName, function) -> {
            if (function.equals(currentFunction) {
                if (!preloadedBeans.contains(beanName)) {
                    beanFactory.getBean(beanName); //触发初始化
                    preloadedBeans.add(beanName);
                }
            }
        });

        return joinPoint.proceed();
    }
}
```

3.扫描
```java
@Component
public class PriorityBeanCollector implements BeanFactoryPostProcessor {
    // 存储 Bean 的优先级和触发函数
    private final Map<String, Integer> beanPriorityMap = new ConcurrentHashMap<>();
    private final Map<String, String> beanTriggerFunctionMap = new ConcurrentHashMap<>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
            if (definition instanceof AnnotatedBeanDefinition) {
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) definition).getMetadata();
                if (metadata.hasAnnotation(LoadPriority.class.getName())) {
                    Map<String, Object> attrs = metadata.getAnnotationAttributes(LoadPriority.class.getName());
                    int priority = (int) attrs.get("value");
                    String function = (String) attrs.get("function");
                    
                    beanPriorityMap.put(beanName, priority);
                    if (priority == 2 && !function.isEmpty()) {
                        beanTriggerFunctionMap.put(beanName, function);
                    }
                }
            }
        }
    }

    public Map<String, Integer> getBeanPriorityMap() {
        return beanPriorityMap;
    }

    public Map<String, String> getBeanTriggerFunctionMap() {
        return beanTriggerFunctionMap;
    }
}
```

4.使用示例
```java
@LoadPriority(value = 2, function = "OrderService.createOrder") 
@Component
public class PaymentService {
    //......
}
```
```java
@Service
public class OrderService {
    @PreloadTrigger 
    public void createOrder() {
        //......
    }
}
```