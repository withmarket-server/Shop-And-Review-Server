package team.bakkas.domaindynamo.entity

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey
import team.bakkas.common.category.Category
import team.bakkas.common.category.DetailCategory
import java.io.Serializable
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

// TODO isBranch, branch attribute 추가
/** Shop Entity class with DynamoDB
 * @author Brian
 * @since 22/05/19
 */
@DynamoDbBean
data class Shop(
    @get:DynamoDbPartitionKey
    @get:DynamoDbAttribute("shop_id")
    var shopId: String = UUID.randomUUID().toString(),
    @get:DynamoDbSortKey
    @get:DynamoDbAttribute("shop_name")
    var shopName: String = "",
    @get:DynamoDbAttribute("is_open")
    var isOpen: Boolean,
    @get:DynamoDbAttribute("open_time")
    var openTime: LocalTime,
    @get:DynamoDbAttribute("close_time")
    var closeTime: LocalTime,
    @get:DynamoDbAttribute("lot_number_address")
    var lotNumberAddress: String,
    @get:DynamoDbAttribute("road_name_address")
    var roadNameAddress: String,
    @get:DynamoDbAttribute("latitude")
    var latitude: Double,
    @get:DynamoDbAttribute("longitude")
    var longitude: Double,
    @get:DynamoDbAttribute("created_at")
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @get:DynamoDbAttribute("updated_at")
    var updatedAt: LocalDateTime?,
    @get:DynamoDbAttribute("average_score")
    var averageScore: Double,
    @get:DynamoDbAttribute("review_number")
    var reviewNumber: Int,
    @get:DynamoDbAttribute("main_image")
    var mainImage: String?,
    @get:DynamoDbAttribute("representative_image_list")
    var representativeImageList: List<String>,
    @get:DynamoDbAttribute("shop_description")
    var shopDescription: String?,
    @get:DynamoDbAttribute("is_branch")
    var isBranch: Boolean,
    @get:DynamoDbAttribute("branch_name")
    var branchName: String?,
    @get:DynamoDbAttribute("shop_category")
    var shopCategory: Category,
    @get:DynamoDbAttribute("shop_detail_category")
    var shopDetailCategory: DetailCategory
) : Serializable