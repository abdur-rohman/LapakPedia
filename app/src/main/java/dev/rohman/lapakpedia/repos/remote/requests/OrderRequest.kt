package dev.rohman.lapakpedia.repos.remote.requests

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    @SerializedName("customer_details")
    val customerDetails: CustomerDetails,

    @SerializedName("item_details")
    val itemDetails: List<ItemDetailsItem>,

    @SerializedName("transaction_details")
    val transactionDetails: TransactionDetails
)

data class CustomerDetails(
    val phone: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("first_name")
    val firstName: String,

    val email: String
)

data class ItemDetailsItem(
    val quantity: Int,
    val price: Int,
    val name: String,
    val id: String
)

data class TransactionDetails(
    val currency: String,

    @SerializedName("gross_amount")
    val grossAmount: Int,

    @SerializedName("order_id")
    val orderId: String
)
