package team.bakkas.domainqueryservice.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.core.CoroutinesUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.bakkas.common.exceptions.shop.ShopNotFoundException
import team.bakkas.domaindynamo.entity.Shop
import team.bakkas.domainqueryservice.repository.ifs.ShopReader
import team.bakkas.domainqueryservice.service.ifs.ShopQueryService

/** Shop에 대한 비지니스 로직을 구현하는 service layer class
 * @param shopReader shop에 대한 cache hit이 구현된 repository
 */
@Service
class ShopQueryServiceImpl(
    private val shopReader: ShopReader
): ShopQueryService {

    /** shopId와 shopName을 이용해서 shop을 가져오는 service method
     * @param shopId shop의 primary key
     * @param shopName shop의 sort key
     * @return @NotNull shop
     * @throws ShopNotFoundException
     */
    @Transactional(readOnly = true)
    override suspend fun findShopByIdAndName(shopId: String, shopName: String): Shop = withContext(Dispatchers.IO) {
        val shopMono = shopReader.findShopByIdAndNameWithCaching(shopId, shopName)
        return@withContext shopMono.awaitSingle()
            ?: throw ShopNotFoundException("Shop is not found!!")
    }

    /** 모든 shop의 리스트를 가져오는 메소드
     * @throws ShopNotFoundException
     * @return list of shop
     */
    @Transactional(readOnly = true)
    override suspend fun getAllShopList(): List<Shop> = withContext(Dispatchers.IO) {
        val shopFlow = shopReader.getAllShopsWithCaching()

        try {
            val firstItem = CoroutinesUtils.monoToDeferred(shopFlow.first()).await()
            checkNotNull(firstItem)
        } catch (e: Exception) {
            throw ShopNotFoundException("Shop is not found!!")
        }

        val shopList = mutableListOf<Shop>()

        shopFlow.buffer()
            .collect {
                val shop = CoroutinesUtils.monoToDeferred(it).await()
                shopList.add(shop!!)
            }

        check(shopList.size != 0) {
            throw ShopNotFoundException("Shop is not found!!")
        }

        shopList
    }
}