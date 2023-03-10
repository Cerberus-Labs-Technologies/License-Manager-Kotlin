package tech.cerberusLabs.rest

enum class LicenseStatus(val httpCode: Int) {
    INACTIVE(403),
    BLOCKED(401),
    EXPIRED(406),
    NOT_FOUND(404)
}
