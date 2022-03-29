

/**
 * 반공변성(Contravariant) 예시
 * */
class ContravariantExample

fun successExample() {
    var listNumber: MutableList<in Number> = mutableListOf(1,2,3)
    var listInt: MutableList<in Int> = listNumber // 컴파일 성공.
}

fun failureExample() {
    var listInt: MutableList<in Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<in Number> = listInt // 컴파일 실패. Type mismatch
}

fun main(args: Array<String>) {

}



