package io.ecosed.framework;

import io.ecosed.framework.ILicenseResultListener;
import io.ecosed.framework.ILicenseV2ResultListener;

interface ILicensingService {
    oneway void checkLicense(long nonce, String packageName, ILicenseResultListener listener);
    oneway void checkLicenseV2(String packageName, ILicenseV2ResultListener listener, in Bundle extraParams);
}