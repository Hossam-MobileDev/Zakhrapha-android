package com.rabapp.zakhrapha

data class SubscriptionType(
    val title: String,       // E.g., "Annual Subscription"
    val description: String, // E.g., "Access for 12 months"
    val price: String,       // E.g., "$99.99"
    val type: SubscriptionTypeEnum
)

enum class SubscriptionTypeEnum {
    MONTHLY,
    ANNUAL,
    LIFETIME
}