package io.ecosed.framework;

interface ILicenseV2ResultListener {
    oneway void verifyLicense(int responseCode, in Bundle responsePayload);
}