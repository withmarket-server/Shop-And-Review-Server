package team.bakkas.common.exceptions.shopReview

/** Shop review를 찾지 못했을 때를 담당하는 예외 class
 * @author Brian
 * @since 22/06/03
 */
class ShopReviewNotFoundException(override val message: String): RuntimeException(message) {

}
