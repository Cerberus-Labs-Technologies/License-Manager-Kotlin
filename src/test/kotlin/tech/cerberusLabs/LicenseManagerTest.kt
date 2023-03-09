package tech.cerberusLabs

import org.junit.jupiter.api.Test

class LicenseManagerTest {


    @Test
    fun testLicenseManager() {
        val licenseManager = LicenseManager("testProduct",true, "C8odAvJSiIIIX0xRRr15zq0NMF", 7, 1)
        licenseManager.validate({
            println("License is valid")
        }) {
            println("License is not valid")
        }
    }
}