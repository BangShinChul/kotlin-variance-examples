


/**
 * 불공변성(Invariant) 예시
 * */
class InvariantExample

fun successExample() {
    var listInt1: MutableList<Int> = mutableListOf(1,2,3)
    var listInt2: MutableList<Int> = listInt1 // 컴파일 성곰.
}

fun failureExample1() {
    var listInt: MutableList<Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<Number> = listInt // 컴파일 에러. Type mismatch
}

fun failureExample2() {
    var listNumber: MutableList<Number> = mutableListOf(1,2,3)
    var listInt: MutableList<Int> = listNumber // 컴파일 에러. Type mismatch
}

fun main(args: Array<String>) {

}







