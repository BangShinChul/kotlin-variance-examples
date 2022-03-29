

/**
 * 공변성(Covariant) 예시
 * */
class CovariantExample

fun successExample() {
    var listInt: MutableList<out Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<out Number> = listInt // 컴파일 성공

    listInt.add(4 as Nothing)
}

fun failureExample() {
    var listInt: MutableList<out Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<out String> = listInt // 컴파일 실패. Type mismatch
}

fun main(args: Array<String>) {

}
