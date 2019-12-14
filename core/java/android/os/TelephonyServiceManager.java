/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.os;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.annotation.SystemApi;

/**
 * Provides a way to register and obtain the system service binder objects managed by the telephony
 * service.
 *
 * <p>Only the telephony mainline module will be able to access an instance of this class.
 *
 * @hide
 */
@SystemApi
public class TelephonyServiceManager {
    /**
     * @hide
     */
    public TelephonyServiceManager() {
    }

    /**
     * A class that exposes the methods to register and obtain each system service.
     */
    public final class ServiceRegisterer {
        private final String mServiceName;

        /**
         * @hide
         */
        public ServiceRegisterer(String serviceName) {
            mServiceName = serviceName;
        }

        /**
         * Register a system server binding object for a service.
         */
        public void register(@NonNull IBinder binder) {
            ServiceManager.addService(mServiceName, binder);
        }

        /**
         * Get the system server binding object for a service.
         *
         * <p>This blocks until the service instance is ready,
         * or a timeout happens, in which case it returns null.
         */
        @Nullable
        public IBinder get() {
            return ServiceManager.getService(mServiceName);
        }

        /**
         * Get the system server binding object for a service.
         *
         * <p>This blocks until the service instance is ready,
         * or a timeout happens, in which case it throws {@link ServiceNotFoundException}.
         */
        @NonNull
        public IBinder getOrThrow() throws ServiceNotFoundException {
            try {
                return ServiceManager.getServiceOrThrow(mServiceName);
            } catch (ServiceManager.ServiceNotFoundException e) {
                throw new ServiceNotFoundException(mServiceName);
            }
        }

        /**
         * Get the system server binding object for a service. If the specified service is
         * not available, it returns null.
         */
        @Nullable
        public IBinder tryGet() {
            return ServiceManager.checkService(mServiceName);
        }
    }

    /**
     * See {@link ServiceRegisterer#getOrThrow}.
     *
     * @hide
     */
    @SystemApi
    public static class ServiceNotFoundException extends ServiceManager.ServiceNotFoundException {
        /**
         * Constructor.
         *
         * @param name the name of the binder service that cannot be found.
         *
         */
        public ServiceNotFoundException(@NonNull String name) {
            super(name);
        }
    }

    /**
     * Returns {@link ServiceRegisterer} for the "telephony" service.
     */
    @NonNull
    public ServiceRegisterer getTelephonyServiceRegisterer() {
        return new ServiceRegisterer("phone");
    }


// TODO: Add more services...
//
//    /**
//     * Returns {@link ServiceRegisterer} for the "subscription" service.
//     */
//    @NonNull
//    public ServiceRegisterer getSubscriptionServiceRegisterer() {
//        return new ServiceRegisterer("isub");
//    }
//
//    /**
//     * Returns {@link ServiceRegisterer} for the "SMS" service.
//     */
//    @NonNull
//    public ServiceRegisterer getSmsServiceRegisterer() {
//        return new ServiceRegisterer("isms");
//    }
}
