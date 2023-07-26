package io.ecosed.framework;

interface ILicenseResultListener {
    oneway void verifyLicense(int responseCode, String signedData, String signature);
}