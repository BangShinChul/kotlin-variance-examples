# 코틀린 변성 정리 (불공변성, 공변성, 반공변성)

Last Edited: 2022년 3월 30일 오전 2:19
Tags: kotlin

---

# 개요

[이펙티브 코틀린](http://www.yes24.com/Product/Goods/106225986) 책을 보는 도중, 아이템 24번(제네릭 타입과 variance 한정자를 활용하라)을 보고

잘 이해가 가지 않아서 코틀린의 가변성에 대해서 정리합니다.

---

# 변성(Variance)이란?

변성(Variance) 이란 **기저 타입(base type)** 이 같으면서 **타입 인자(type argument)** 가 다른 경우 서로 어떤 관계에 있는지 설명하는 개념입니다.

참고로 기저 타입(base type)은 **원시 타입**이라고도 부르며,

타입 인자(type argument)는 **제네릭 타입**이라고도 부릅니다.

```kotlin
List<Int>
List<Number>

// 여기서 기저 타입(base type)은 List 이며,
// 타입 인자(type argument)는 <Int>, <Number> 입니다.
```

우리는 왜 변성이라는 개념에 대해서 알아야 할까요?

`List<Int>`와 `List<Number>` 를 보았을 때 서로의 관계는 어떠할까요?

우선 `Int`는 `Number`의 서브 타입 인 것은 확실합니다.

`Int` 클래스는 `Number` 클래스를 상속하고 있기 때문입니다.

![Primitives kt 파일 내에 존재하는 코드 참조](https://user-images.githubusercontent.com/26675063/160671423-8100cb3b-a011-4448-9dab-3bb05017f4ef.png)

> Primitives.kt 파일 내에 존재하는 코드 참조.

<br>


그렇다면 `List<Int>`도 `List<Number>`의 서브 타입 일까요?

아닙니다.

그렇기 때문에 우리는 변성의 개념에 대해서 알아야 합니다.

<br><br>

# 서브 타입 & 수퍼 타입

변성에 대해서 알아보기에 앞서,

서브 타입과 수퍼 타입에 대해서 좀 더 자세히 알아보겠습니다.

서브 타입(subtypes)과 수퍼 타입(supertypes)은 상속 관계를 통해 정의됩니다.

<br>


아래 예시 코드를 보겠습니다.

```kotlin
open class Coworker() {
    val companyName = "Market Kurly"
    val department = "Commerce Development"
}

class Tomas: Coworker() {
    val name = "Tomas"
    val position = "Backend Development"
}
```

위의 예시 코드에서

`Tomas` 클래스는 `Coworker` 클래스를 상속받았으므로

`Tomas` 클래스는 `Coworker` 클래스의 **서브 타입(subtypes)** 입니다.

<br>

수퍼 타입(supertypes)은 서브 타입의 반대 개념입니다.

`Coworker` 클래스는 `Tomas` 클래스가 상속받았으므로

`Coworker` 클래스는 `Tomas` 클래스의 **수퍼 타입(supertypes)** 입니다.

<br>

즉, 어떤 클래스가 상속을 받았고

어떤 클래스가 상속 당했는지(?)에 따라서

서브 타입과 수퍼 타입이 결정됩니다.

<br>

조금 헷갈릴 수도 있습니다만, 걱정마세요!

어떠한 클래스가 서브 타입인지 확실하게 알 수 있는 규칙이 있습니다.

규칙 내용은 이렇습니다.

```kotlin
타입 A의 값이 필요한 모든 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면

타입 B는 타입 A의 서브 타입이다.
```

위의 규칙이 정말 맞는지 수퍼 타입과 서브 타입의 관계를 코드로 이해해보겠습니다.

아래 코드는 정상적으로 컴파일이 됩니다.

```kotlin
var coworker = Coworker()
var tomas = Tomas()

coworker = tomas // 정상
```

규칙 내용의 `A`와 `B`를 `Coworker`와 `Tomas`로 치환해볼까요?

```kotlin
타입 Coworker의 값이 필요한 모든 장소에 어떤 타입 Tomas의 값을 넣어도 아무 문제가 없다면

타입 Tomas는 타입 Coworker의 서브 타입이다.
```

규칙의 내용이 참(True)이죠?

따라서 `Tomas` 클래스는 `Coworker` 클래스의 서브 타입이 맞습니다.

<br>

하지만, 아래 코드는 `Type mismatch` 에러가 발생합니다.

```kotlin
var coworker = Coworker()
var tomas = Tomas()

tomas = coworker // Type mismatch
```

타입 `Tomas`의 값이 필요한 모든 장소에 어떤 타입 `Coworker`의 값을 넣었을 때 `Type mismatch` 문제가 생겼습니다.

따라서 `Coworker` 클래스는 `Tomas` 클래스의 서브 타입이 아닙니다.

<br><br>

# 불공변성(invariant)

불공변성(Invariant)은 무공변성 이라고도 부릅니다.

이 불공변성은 **제네릭 타입으로 만들어지는 타입들이 서로 관련성이 없다**는 의미를 나타냅니다.

즉, `MutableList<Int>`와 `MutableList<Number>`는 불공변성 입니다.

<br>

정말인지 확인해볼까요?

만약 `MutableList<Int>`와 `MutableList<Number>`가 서로

`Int`와 `Number` 사이의 관계처럼 서로 관련성이 있다면 (서브 타입 or 수퍼 타입)

`MutableList<Number>` 타입의 변수에 `MutableList<Int>` 타입 값을 할당할 수 있어야 할 것입니다.

```kotlin
/**
 * 불공변성(Invariant) 예시
 * */
class InvariantExample
fun main(args: Array<String>) {
    var listInt: MutableList<Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<Number> = listInt // 컴파일 에러. Type mismatch
}
```

![Invariant 예시 1](https://user-images.githubusercontent.com/26675063/160671436-24785151-f590-4ea6-821d-cc13d9c59faa.png)

> MutableList<Number> 타입의 변수에 MutableList<Int> 타입의 값을 할당하는 예제

<br>

위 예제와 같이 `Type mismatch` 에러를 통해 컴파일 에러가 발생하는 것을 확인할 수 있습니다.

물론, 반대로 `MutableList<Int>` 타입의 변수에 `MutableList<Number>` 타입 값을 할당할 수도 없습니다.

![Invariant 예시 2](https://user-images.githubusercontent.com/26675063/160671443-16168ac6-4000-4551-b49d-b101a487f3f2.png)


> MutableList<Int> 타입의 변수에 MutableList<Number> 타입의 값을 할당하는 예제

<br>

따라서 `MutableList<Number>` 와 `MutableList<Int>`는 서로 전혀 관련성이 없음을 알 수 있습니다.

왜 관련성이 없을까요?

그것은 제네릭의 특성 때문입니다.

제네릭 클래스의 인스턴스는 기저 타입(base type)과 타입 인자(type argument)가 결합된 것이 자신이 타입이 됩니다.

예 :

- MutableList<Number> → MutableListNumber
- MutableList<Int> → MutableListInt

따라서 `<>` 으로 나타낸 제네릭 타입 인자(type argument) 간의 수퍼-서브 관계가 있더라도 **컴파일러가 인식하지 못합니다.**

참고 : [Generic Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)

<br><br>

# 공변성(Covariant)

그렇다면 제네릭에서는 수퍼-서브 타입에 대해서는 관계를 정해줄 수는 없는걸까요?

`MutableList<Number>` 와 `MutableList<Int>` 의 관계를 `Number`와 `Int`처럼 서브 타입 or 수퍼 타입 관계로 만들어줄 수는 없을까요?

“`Int`가 `Number`의 서브 타입일 때, `MutableList<Int>` 는 `MutableList<Number>` 의 서브 타입이다”

와 같이 제네릭 간의 **서브 타입 관계**를 정해주고 싶을 때에는 `out` 키워드를 사용하면 됩니다.

```kotlin
/**
 * 공변성(Covariant) 예시
 * */
class CovariantExample
fun main(args: Array<String>) {
    var listInt: MutableList<out Int> = mutableListOf(1,2,3)
    var listNumber: MutableList<out Number> = listInt // 컴파일 성공
}
```

![Covariant 예시 1](https://user-images.githubusercontent.com/26675063/160671613-071687a9-7b51-4c7f-b09e-3ebdaa279b0d.png)

> MutableList<out Number> 타입의 변수에 MutableList<out Int> 타입의 값을 할당하는 예제

<br>

이것이 가능한 이유는 `out` 키워드를 사용하여 `Int` 타입과 `Number` 타입의 관계를 정해주었기 때문입니다.

`out` 키워드는 두 제네릭의 타입 인자(type argument) 간의 수퍼-서브 관계를 컴파일러가 고려해주도록 합니다.

이처럼 `MutableList<out Int>` 클래스와 `MutableList<out Number>` 클래스 같은 관계를 **공변적** 이라고 표현합니다.

<br>

그럼 `MutableList<out Number>`를 `MutableList<out String>`으로 바꿔도 동일하게 공변적일까요?

한번 실험해봅시다.

![Covariant 예시 2](https://user-images.githubusercontent.com/26675063/160671619-7af2042f-4753-4d3a-8d76-3726d02243ea.png)


> MutableList<out String> 타입의 변수에 MutableList<out Int> 타입의 값을 할당하는 예제

<br>

위 예제와 같이 `Type mismatch` 에러를 통해 컴파일 에러가 발생하는 것을 확인할 수 있습니다.

`out` 키워드를 통해 컴파일러에게 타입 인자(type argument) 간의 수퍼-서브 관계를 고려하도록 하였지만,

`String`과 `Int` 사이의 수퍼-서브 관계는 없기 때문에 `Type mismatch` 에러가 발생하였습니다.

<br><br>

# 반공변성(Contravariant)

반공변성이란, 공변성과 반대되는 개념입니다.

예를 들어 `Int` 클래스와 `Number` 클래스의 관계를 뒤집어서

`Number` 클래스가 `Int` 클래스의 서브 타입이 되도록 합니다.

즉, 제네릭에서 수퍼-서브 타입 관계와 반대가 되는 관계를 설정합니다.

<br>

반공변성 관계를 지정해주려면 `in` 키워드를 사용하면 됩니다.

“`Int`가 `Number`의 서브 타입일 때, `MutableList<Number>` 는 `MutableList<Int>` 의 서브 타입이다”

와 같이 **원래 관계와 반대되는 제네릭 간의 서브 타입 관계를 정해주고 싶을 때**에는 `in` 키워드를 사용하면 됩니다.

```kotlin
/**
 * 반공변성(Contravariant) 예시
 * */
class ContravariantExample
fun main(args: Array<String>) {
    var listNumber: MutableList<in Number> = mutableListOf(1,2,3)
    var listInt: MutableList<in Int> = listNumber // 컴파일 성공.
}
```

![Contravariant 예시](https://user-images.githubusercontent.com/26675063/160671694-ec5085d7-efcc-479b-8f21-6bf9998335f9.png)

> MutableList<in Int> 타입의 변수에 MutableList<in Number> 타입의 값을 할당하는 예제

<br>

이처럼 `MutableList<in Int>` 클래스와 `MutableList<in Number>` 클래스 같은 관계를 **반공변적** 이라고 표현합니다.

---

# 참고 링크

[Generics: in, out, where | Kotlin](https://kotlinlang.org/docs/generics.html)

[[kotlin] 제너릭 변성(variance) 정리](https://namget.tistory.com/entry/kotlin-%EC%A0%9C%EB%84%88%EB%A6%AD%ED%83%80%EC%9E%85-%EC%A0%95%EB%A6%AC)

[Kotlin Generics - 변성](https://medium.com/hongbeomi-dev/kotlin-generics-%EB%B3%80%EC%84%B1-f11e4efcb486)

[](https://thecodinglog.github.io/java/2020/12/15/java-generic-wildcard.html)

[Type system - JetBrains Academy - Learn programming by building your own apps](https://hyperskill.org/learn/step/9689#step-title)

[Kotlin Types, Subtypes, and Supertypes vs Classes, Subclasses and Superclasses](https://stackoverflow.com/questions/64033894/kotlin-types-subtypes-and-supertypes-vs-classes-subclasses-and-superclasses)

[코틀린 변성에 대한 이해](https://leejaeho.dev/posts/kotlin-generic-variance/)

[자바의 제네릭 타입 소거, 리스트에 관하여 (Java Generics Type Erasure, List)](https://jyami.tistory.com/99)

[Type Erasure](https://docs.oracle.com/javase/tutorial/java/generics/erasure.html)
