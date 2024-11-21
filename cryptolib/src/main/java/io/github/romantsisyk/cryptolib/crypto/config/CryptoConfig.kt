package io.github.romantsisyk.cryptolib.crypto.config


class CryptoConfig private constructor(
    val keyAlias: String,
    val requireUserAuthentication: Boolean,
    val keyRotationIntervalDays: Int,
    val keyValidityDays: Int
) {
    data class Builder(
        private val keyAlias: String
    ) {
        private var requireUserAuthentication: Boolean = false
        private var keyRotationIntervalDays: Int = 90
        private var keyValidityDays: Int = 365

        fun requireUserAuthentication(requireAuth: Boolean) = apply {
            this.requireUserAuthentication = requireAuth
        }

        fun keyRotationIntervalDays(days: Int) = apply {
            this.keyRotationIntervalDays = days
        }

        fun keyValidityDays(days: Int) = apply {
            this.keyValidityDays = days
        }

        fun build() = CryptoConfig(
            keyAlias,
            requireUserAuthentication,
            keyRotationIntervalDays,
            keyValidityDays
        )
    }
}
