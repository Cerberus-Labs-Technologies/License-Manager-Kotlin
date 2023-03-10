package tech.cerberusLabs

import org.junit.jupiter.api.Test

class LicenseManagerTest {


    @Test
    fun testLicenseManager() {
        val licenseManager = LicenseManager("testProduct",true)
        licenseManager.validate({
            println("License is valid")
        }) {
            println("License is not valid")
        }
    }
}