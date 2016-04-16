package com.xfyre.tapestry5.xtensions.services;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.ioc.services.RegistryShutdownHub;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.upload.internal.services.MultipartDecoderImpl;
import org.apache.tapestry5.upload.services.MultipartDecoder;
import org.slf4j.Logger;

public class XtensionsModule {
    @Startup
    public static void initT5XtensionsModule ( Logger logger ) {
        logger.info ( "Initializing t5xtensions" );
    }

    @Contribute(ComponentClassResolver.class)
    public static void contributeComponentClassResolver ( Configuration<LibraryMapping> configuration ) {
        configuration.add ( new LibraryMapping ( "t5xtensions", "com.xfyre.tapestry5.xtensions" ) );
    }

    private static final AtomicBoolean needToAddShutdownListener = new AtomicBoolean ( true );

    @Scope(ScopeConstants.PERTHREAD)
    public static MultipartDecoder buildMultipartDecoder2 ( RegistryShutdownHub shutdownHub,
            @Autobuild MultipartDecoderImpl multipartDecoder ) {

        if ( needToAddShutdownListener.getAndSet ( false ) ) {
            shutdownHub.addRegistryShutdownListener ( new Runnable () {
                @Override
                @SuppressWarnings("deprecation")
                public void run () {
                    org.apache.commons.io.FileCleaner.exitWhenFinished ();
                }
            } );
        }

        return multipartDecoder;
    }

    public static void contributeServiceOverride (
            @InjectService("MultipartDecoder2") MultipartDecoder multipartDecoder,
            @SuppressWarnings("rawtypes") MappedConfiguration<Class, Object> overrides ) {

        overrides.add ( MultipartDecoder.class, multipartDecoder );
    }

    @Contribute(ComponentClassTransformWorker2.class)
    public static void provideWorkers ( OrderedConfiguration<ComponentClassTransformWorker2> workers ) {
        workers.addInstance ( "ModalIntegrationWorker", ModalIntegrationWorker.class );
    }

}
