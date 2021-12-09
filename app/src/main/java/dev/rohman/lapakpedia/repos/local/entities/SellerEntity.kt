package dev.rohman.lapakpedia.repos.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.rohman.lapakpedia.models.SellerModel

@Entity(
    tableName = "sellers",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SellerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "order_id", defaultValue = "0", index = true) val orderId: Long,
    @ColumnInfo(name = "name", defaultValue = "") val name: String,
    @ColumnInfo(name = "image", defaultValue = "") val image: String,
    @ColumnInfo(name = "address", defaultValue = "") val address: String,
)

fun SellerEntity.toModel() = SellerModel(id, orderId, name, image, address)