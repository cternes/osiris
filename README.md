osiris
======

A JavaEE 7 CDI interceptor which provides monitoring capabilities for called methods. 

It provides an easy way to log what arguments were passed into a method and what was returned by the method.
On top of this, the execution time for a method invocation is measured and logged.

## Requirements ##
Osiris uses [slf4j](http://slf4j.org) to log method invocations. You need to make sure that your project uses `slf4j` API for logging to your desired logging framework.

## Usage ##
The usage is fairly easy, just drop the library as a dependency into your project and annotate the methods you wish to monitor.

__NOTE:__ Please note that monitoring only works for CDI managed beans and public methods.

```java
@Stateless
public class Calculator {
    
    @Monitored(layer = "BUSINESS", useCase = "Adding numbers")
    public int Add(int x, int y) {
        return x + y;
    }
}
```

This will result in the following logging messages:

    BUSINESS - UseCase: [Adding numbers] Thread: [http-listener-1(2)] Method: [my.package.Calculator:Add] Args: [10, 20]]]
    BUSINESS - UseCase: [Adding numbers] Thread: [http-listener-1(2)] Method: [my.package.Calculator:Add] Took: [61 ms] Returned: [30]]]
    
__NOTE:__ The `layer` parameter of the annotation is used as the logger name, so please make sure that the logger is configured properly in your logging framework. That means, if you use `Business` as the layer name, you need to configure a logger called `Business` in your logging framework. 

All logging messages will be logged in the `DEBUG` level.
