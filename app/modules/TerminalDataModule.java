package modules;

import com.google.inject.AbstractModule;


public class TerminalDataModule extends AbstractModule {
    @Override
    protected void configure() {
        // We bind the "ExampleTask" class eagerly so it'll be started when the application starts
        // see: https://www.playframework.com/documentation/2.5.x/JavaDependencyInjection#Eager-bindings
        bind(TerminalDataScheduler.class).asEagerSingleton();
        bind(NorwayDataScheduler.class).asEagerSingleton();
    }
}