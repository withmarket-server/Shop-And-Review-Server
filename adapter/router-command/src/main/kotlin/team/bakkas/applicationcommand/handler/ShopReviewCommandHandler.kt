package team.bakkas.applicationcommand.handler

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull
import team.bakkas.applicationcommand.extensions.toEntity
import team.bakkas.clientcommand.shopReview.ShopReviewCommand
import team.bakkas.common.ResultFactory
import team.bakkas.common.exceptions.RequestBodyLostException
import team.bakkas.common.exceptions.RequestParamLostException
import team.bakkas.servicecommand.service.ifs.ShopReviewCommandService
import team.bakkas.servicecommand.validator.ShopReviewValidator
import team.bakkas.eventinterface.eventProducer.ShopReviewEventProducer

/** ShopReview에 대한 command handler class
 * @param shopReviewValidator shopReview에 대한 검증을 담당하는 bean
 * @param shopReviewEventProducer
 */
@Component
class ShopReviewCommandHandler(
    private val shopReviewValidator: ShopReviewValidator,
    private val shopReviewEventProducer: ShopReviewEventProducer
) {

    // shopReview를 하나 생성하는 메소드
    suspend fun createReview(request: ServerRequest): ServerResponse = coroutineScope {
        // 비동기적으로 reviewDto를 body로부터 뽑아온다
        val reviewCreateRequest = request.bodyToMono(ShopReviewCommand.CreateRequest::class.java)
            .awaitSingleOrNull()

        // body가 유실되어있는지 검증
        checkNotNull(reviewCreateRequest) {
            throw RequestBodyLostException("Body is lost!!")
        }

        shopReviewValidator.validateCreatable(reviewCreateRequest)

        // dto -> entity 변환
        val generatedReview = reviewCreateRequest.toEntity()

        // Kafka에 리뷰 생성 이벤트를 전파한다
        shopReviewEventProducer.propagateCreatedEvent(generatedReview)

        return@coroutineScope ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(ResultFactory.getSuccessResult())
    }

    // shopReview를 삭제하는 메소드
    suspend fun deleteReview(request: ServerRequest): ServerResponse = coroutineScope {
        val reviewId = request.queryParamOrNull("id") ?: throw RequestParamLostException("reviewId is lost")
        val reviewTitle = request.queryParamOrNull("title") ?: throw RequestParamLostException("reviewTitle is lost")

        // 해당 review가 삭제 가능한지 검증
        shopReviewValidator.validateDeletable(reviewId, reviewTitle)

        val deletedEvent = ShopReviewCommand.DeletedEvent.of(reviewId, reviewTitle)

        // Kafka에 삭제 이벤트를 발행하여 Kafka 내부에서 삭제를 처리한다
        shopReviewEventProducer.propagateDeletedEvent(deletedEvent)

        return@coroutineScope ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(ResultFactory.getSuccessResult())
    }
}