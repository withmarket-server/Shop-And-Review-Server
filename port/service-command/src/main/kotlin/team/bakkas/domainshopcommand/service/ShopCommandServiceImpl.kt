package team.bakkas.domainshopcommand.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.withContext
import org.springframework.core.CoroutinesUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import team.bakkas.clientcommand.dto.ShopCommand
import team.bakkas.domaindynamo.entity.Shop
import team.bakkas.domainshopcommand.service.ifs.ShopCommandService
import team.bakkas.repository.ifs.dynamo.ShopDynamoRepository

/** shop의 command query를 담당하는 비지니스 로직을 정의하는 service 클래스
 * @param shopDynamoRepository dynamoDB에 접근하는데 사용하는 Data access layer의 repository
 * @param shopValidator shop이 올바른지 검증해주는 validator
 */
@Service
class ShopCommandServiceImpl(
    private val shopDynamoRepository: ShopDynamoRepository
) : ShopCommandService {

    /** shop을 생성하는 비지니스 로직을 정의하는 메소드
     * @param shop 생성 가능성이 검증된 이후로 들어오는 shop entity 객체
     */
    @Transactional
    override suspend fun createShop(shop: Shop): Shop = withContext(Dispatchers.IO) {
        val shopMono = shopDynamoRepository.createShop(shop)

        shopMono.awaitSingleOrNull()

        shop
    }

    // TODO shop을 수정하는 메소드

    // TODO shop을 삭제하는 메소드
}