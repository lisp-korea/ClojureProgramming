# PART Ⅲ Tools, Platform, and Projects
## CHAPTER 8. Organizing and Building Clojure Projects

아이러니하게도, 새로운 유망 프로그래밍 언어를 채택의 가장 어려운 측면 중 하나는 종종 언어 자체 이다. 엔드유저와 설치된 웹 어플리케이션 서버에 정리하고 라이브러리와 같은 다른 프로그래머들에 의해, 배포 및 사용할 수 있는 산출물로 그 새로운 언어로 작성한 코드베이스를 구축 할 필요가 있다. 이러한 문제의 특성은 기존 프로젝트의 일환으로 또는 완전히 새로운 노력의 일환과 특정 배포 요구 사항은 새로운 언어를 사용하는 여부에 따라 크게 달라질 수 있다.

우리는 종종 자신의 중요성을 능가 할 수 이러한 영역의 일부에 있는 모든 당신이 당신의 프로젝트를 정리하고 Clojure에서의 노동의 열매를 재배포 할 수있는 방법, 그리고 의견의 차이를 커버하는 것은 불가능하다

그러나 그것은 당신을 설정하는 우리 시현이다.
Clojure의 커뮤니티에서의 일반적인 접근 방식에 따라 좋은 경로이다.
이 장에서, Clojure의 코드베이스와 Clojure의 커뮤니티, Leiningen과 메이븐에서 가장 인기있는 두 빌드 도구를 사용하여 Clojure의 프로젝트의 빌드 문제를 해결하기 위해 존재하는 가장 좋은 방법, 당신이 구조에 대해 생각하는 방법에 대한 몇 가지 일반적인 힌트를 줄 것이다.

### Project Geography

빌드의 메커니즘에 들어가기 전에, 우리는 먼저 파일의 물리적 배치뿐만 아니라 당신의 코드베이스의 기능 조직과 관련하여 Clojure의 프로젝트를 구성하는 방법을 설정해야한다. 이는 네임 스페이스에 대한 이야기를 의미한다.

#### Defining and Using Namespaces

20페이지의 “Namespaces”에서 Clojure의 네임스페이스를 언급했다.

* 자바 클래스 이름과 var에 심볼의 동적 매핑되며, 모든 사용자가 지정하는 값(가장 많이 function에, 일정 데이터 및 참조 형식)을 포함.
* 파이썬과 루비에서 자바의 패키지와 모듈을 거의 유사함.

모든 Clojure 코드는 네임스페이스 내에 정의된다. 당신이 당신의 자신을 정의하는 데 소홀히 하는 경우, 사용자가 정의한 모든 var 기본 사용자 이름 공간에 매핑됩니다. 당신이 마지막으로 하고 다른 사람이 사용할 수 뭔가를 구축하고자 하면 REPL 상호 작용의 많은 비용이 있지만, 그것은 거의 결코 좋은 생각이다. 당신의 Clojure의 코드베이스에서 우리는 그들이 각각의 소스 파일에 매핑하는 방법, 관용구 네임 스페이스를 정의하는 방법, 고차원의 구조 및 조직을 제공하는 데 사용하는 방법을 알고 싶어 한다. Clojure의 이산 (REPL에서 매우 유용) 네임 스페이스의 사소한 조작을 위한 기능뿐만 아니라, 우리가 다른 네임 스페이스와 자바 클래스에 네임 스페이스의 이름, 최상위 문서 및 종속성에 선언하는 데 사용할 수 있는 하나의 매크로에 이러한 기능의 통합을 제공합니다.

**in-ns.** **def**와 (**defn** 같은) 모든 그들의 변수들은 ***ns***에 바운드된 현재 네임스페이스 내에 변수를 정의한다.

> *ns*  
;= #<Namespace user>   
(defn a [] 42)   
;= #'user/a

in-ns 를 사용하여, 우리는 다른 네임 스페이스로 (아직하지 않을 경우 이를 생성하여) 전환 할 수 있습니다. 따라서 우리가 다른 네임 스페이스에 변수를 정의하는게 가능합니다:

> (in-ns 'physics.constants)  
;= #<Namespace physics.constants>  
(def ^:const planck 6.62606957e-34)  
;= #'physics.constants/planck

그러나, 우리는 뭔가 새로운 네임 스페이스에 다른(?) 것을 신속하게 발견 할 것이다 :

> (+ 1 1)  
;= #<CompilerException java.lang.RuntimeException:  
;= Unable to resolve symbol: + in this context, compiling:(NO_SOURCE_PATH:1)>

'+' 함수 (clojure.core 네임 스페이스의 모든 다른 함수)는 우리가 기본 user 네임스페이스에 있는 함수를 사용할 수 없습니다 - 네임 스페이스 자격을 갖춘 심볼을 사용하여 액세스 할 수 있습니다 :

> (clojure.core/range -20 20 4)  
;= (-20 -16 -12 -8 -4 0 4 8 12 16)

네임 스페이스가 var에 심볼 매핑을 것을 기억하십시오; in-ns 가 네이밍한 네임스페이스로 전환하는 것은 모두이다. 특수 형태 (def, var 등을 포함)를 계속 사용할 수 있지만, 우리는 합리적으로 간결하게 코드를 사용하기 위해 우리의 새 네임 스페이스에 이름 vars 다른 네임 스페이스와 map에서 코드를 로드해야 합니다.

**refer.** 네임 스페이스가 이미 로드되었다고 가정 할 때, 우리는 우리의 네임 스페이스의 vars에 매핑을 추가 참조 할 수 있습니다. 우리는 이전에 user 네임 스페이스에 더미 함수 **a**를 정의했다. 우리는 **a**에 더 쉽게 액세스 할 수 있도록, **user**의 public vars의 모든 우리의 빈 공간에 매핑을 설정할 수 있습니다 :

> user/a  
;= #<user$a user$a@6080669d>  
(clojure.core/refer 'user)  
;= nil  
(a)  
;= 42

**a**는 지금 로컬로 정의 된 것처럼, **user/a** 에 우리가 사용할 수 있는 var에 우리의 현재 네임 스페이스 내에서 매핑됩니다. 즉, 확실히 다른 네임 스페이스에서 var에 액세스 할 수 사방 네임 스페이스 자격을 갖춘 기호를 사용하는 것보다 쉽다.
당신은 간단한 "import"보다 더 많은 작업을 수행 할 수 있습니다 : 당신은 각각 그들의 선택적 키워드 **:exclude, :only, :rename**의 인수를 사용하여 현재 이름 공간에 매핑 할 때 특정 var은 제외 포함하거나 이름을 바꾸도록 var을 명세할 수 있습니다. 예를 들어, 이 clojure.core를 **refer** 할 때, 일부 기능을 제외한다면 지역적으로 다른 이름으로 산술 연산자의 일부를 map 참조한다 :

> (clojure.core/refer 'clojure.core  
:exclude '(range)  
:rename '{+ add  
ㅡ sub  
/ div  
＊ mul})  
;= nil  
(-> 5 (add 18) (mul 2) (sub 6))  
;= 40  
(range -20 20 4)  
;= #<CompilerException java.lang.RuntimeException:  
;= Unable to resolve symbol: range in this context, compiling:(NO_SOURCE_PATH:1)>

이제 우리는 **clojure.core**에서 모든 public functions(**refer** 는 소스 네임스페이스에서 어떤 private vars 도 가져오지 않는다.)를 사용할 수 있는 (우리가 포함하지 않은 **range** 제외), 우리는 연산 기능의 일부에 대해 다른 이름을 사용하고 있습니다. 항상 **clojure.core** 사전로드 (**user** 네임 스페이스에서 **참조** 함)하는 동안, 우리는 종종 그 이상이 필요합니다, 우리는 현명하게 당신의 코드베이스를 구성하기 위해 여러 네임 스페이스에게 자신을 정의 할 수 있습니다. 우리는 로딩 네임 스페이스를 위한 시설이 필요합니다.

***
**refer**는 거의 직접적으로 사용되지 않고, 그 효과 및 옵션에 널리 사용되는 use를 통해 사용할 수 _있습니다_.
***

**require and use.** 일부 코드가 다른 네임 스페이스에 public vars에 정의 된 함수나 데이터의 사용해야 하는 경우, **require**와 **use**가 사용됩니다 :
1. 문제가 로드되는 네임 스페이스를 확인한다.
2. 선택적으로 네임 스페이스의 이름에 대한 별칭을 설정한다.
3. 자격없는 다른 네임 스페이스의 vars를 참조하는 코드를 허용하는 암시적 **refer**의 사용을 트리거한다.

**require** 는 (1)과 (2)를 제공하고; **use** 는 그것의 상단에 구성되어, 간결한 방법으로 (3)의 제공을 **참조** 한다.

새로운 REPL을 시작하자, 우리가 **clojure.set** 네임 스페이스의 **union** 함수를 사용해본다:

> (clojure.set/union #{1 2 3} #{4 5 6})  
;= #<ClassNotFoundException java.lang.ClassNotFoundException: clojure.set>

잠깐, 해당 네임 스페이스는 아직 사전 로드 되지 않았다. **clojure.core** 만 로드되었다. classpath로부터 **clojure.set** 네임스페이스를 로는 하는게 **요구** 된다. 그 후, 우리는 그 네임스페이스에서 어떤 함수를 사용할 수 있습니다 :

> (require 'clojure.set)  
;= nil  
(clojure.set/union #{1 2 3} #{4 5 6})  
;= #{1 2 3 4 5 6}

vars 이름을 정규화 된 기호를 사용하는 있는 것은 특히, 그래도 통증이 있을 수 있습니다. 당신이 사용하는 라이브러리는 긴 또는 세그먼트의 번호가 네임 스페이스를 제공한다. 고맙게도, **require** 는 네임스페이스를 위한 별명을 명세하는 방법으로 제공한다.

> (require '[clojure.set :as set])  ①
;= nil  
(set/union #{1 2 3} #{4 5 6})  
;= #{1 2 3 4 5 6}

① 벡터 인자들은 _libspecs_ 라 불리는 **require** 와 **use** 에 제공된다.

그들은 라이브러리가 현재의 네임 스페이스 내에서 언급되고 로드되는 방법을 명세합니다.
당신이 공통의 접두사를 공유하는 여러 네임 스페이스를 필요로 할 필요가 있을 때, 당신은 첫 번째 요소는 네임 스페이스 접두사이고 나머지 요소는 로드하고자 하는 네임 스페이스를 지정 나머지 세그먼트 곳 순차적 수집을 필요로 제공 할 수 있습니다. 그래서, 우리는 모두 clojure.set을 필요로하고 싶었 경우 clojure.string, 우리는 Clojure의 접두사를 반복 할 필요가 없습니다.

> (require '(clojure string [set :as set]))

**use**는 **require**의 용량의 모두를 제공합니다. 기본적인 것을 제외하고, 그것은 로드된 후 네임스페이스를 **참조**한다. 그래서 **(use 'clojure.xml)**는 다음과 동일하다.

> (require 'clojure.xml)  
(refer 'clojure.xml)

게다가, **use** 는 **참조**하는 그 인자들의 모든 것을 통과시켜, **:exclude, :only, :rename** 의 최대한의 것을 활용할 수 있습니다. **clojure.string**과 **clojure.set**의 사용이 필요한 곳에서 시나리오를 그려보자:

1. 우리의 현재 네임스페이스에서 아래로 vars의 모든 것을 **참조**하길 원하지만,
2. **clojure.string**의 지역 함수들과 충돌이 있고; 간단한 네임스페이스 별명(**require**의 **:as** 사용) 이 작동되고,
3. **clojure.string/join**을 많이 사용하길 원하고, 우리의 현재 네임스페이스에서 어떠한 함수와도 충돌하지 않으며, 이 경우에 네임스페이스 별명을 피할 수 있다.
4. **clojure.string**과 **clojure.set**은 둘다 **join**이 정의되어 있다. 그들을 참조하려 하면, 오류가 발생할 것이다. 그래서 **clojure.string/join**을 선호한다.

**use**는 쉽게 이러한 기준을 수용 할 수 있습니다.

> (use '(clojure [string :only (join) :as str]  
[set :exclude (join)]))  
;= nil  
join  
;= #<string$join clojure.string$join@2259a735>  
intersection  
;= #<set$intersection clojure.set$intersection@2f7fc44f>  
str/trim  
;= #<string$trim clojure.string$trim@283aa791>

우리는 **clojure.string**의 **join**을 네임스페이스 명시 없이 사용할 수 있습니다. 그러나 **clojure.set**은 우리 네임스페이스에서 나머지들(**intersection**을 포함해)을 참조합니다. **clojure.string** 네임스페이스는 **str** 별명을 통해 활용 가능합니다.

***
**Using require, refer, and use Effectively**

 특히 자바에서 **import**되고 루비에서 **require**하는 무딘 도구에 비교하여, 콘서트에 이러한 기능은 많은 미묘한 옵션을 제공합니다. 효과적이고 관용적인 Clojure의 새로운 일부 확인 포인트가 될 수 있습니다.
 각 네임스페이스를 위한 별명을 일반적으로, 좋은 초기화는 항상 **require**를 사용하는 것입니다.

> (require '(clojure [string :as str]  
[set :as set]))

이것은 파이선의 import sys, os 과 대체로 유사합니다. 네임 스페이스는 일반적으로 (파이썬에서 일반적인 단일 토큰 모듈 이름에 비해) 여러 세그먼트를 가지고 있기 때문에, Clojure의 필요 네임 스페이스에 대한 기본 별칭을 제공하지 않지만, 당신이 사용되는 별명을 제어 할 수 없습니다. 문제의 공간이 짧고, 또는 당신은 단지 그것에서 몇 번 vars를 사용하는 경우 별칭이 완전히 적합하지 않고 물론, 다음 bare가 **필요**합니다. 또 다른 일반적으로 권장되는 패턴은 네임 스페이스 별칭 현재 네임 스페이스에 참조하는 vars의 명시적 포함 목록과 함께 사용을 선호하는 것입니다 :

> (use '[clojure.set :as set :only (intersection)])

**use**가 양식은 당신이 하나의 사용 형태로 모든 네임 스페이스 참조를 통합 할 수 있다는 것을 의미합니다. **use**하고 **require**할 필요가 제공하는 모든 기능의 상위 집합을 제공합니다. 비록 당신이 그렇지 않으면 앨리어싱을 사용할 수있는 곳이 양식을 **요구**, 해당 사용 형태는 더 이상 거의없고 당신이 **:only** 인수를 아주 쉽게 참조 기능을 추가 할 수 있습니다.

명시적으로 현재의 이름 공간에 **:only** 대해 참조한다 함수의 이름을 옵션 어떤 경우에, 그 사용 용도가 제약되지 않도록하는 것이 좋다, 즉, 일반적으로 포함하지 않는 것들이다. 이렇게 하면 코드를 사용합니다 어떤 다른 네임 스페이스의 일부가 명확하게하고, 상류 라이브러리를 변경하고 이미 로컬로 선언 할 수 있다. 기능을 추가로 최대 자를수 있는 이름 충돌 경고를 방지 할 수 있습니다.
***

**import.** Clojure가 네임 스페이스가 자주 규범적으로 vars에 map symbol을 매핑하는 동안, 여러 다른 네임 스페이스에 정의 된, 그들은 또한 자바 클래스와 인터페이스에 기호를 매핑합니다. 당신은 현재의 네임 스페이스와 같은 매핑을 추가하는데 **import**를 사용할 수 있습니다.
**import** 는 가져올 클래스의 전체 이름을 가져올 인수, 또는 패키지를 설명하는 순차적 컬렉션, 클래스의 풀네임을 인자로 가져온다. 클래스를 가져 오면 현재 네임 스페이스 내에서 사용하기 위한 "짧은 이름"을 사용할 수 있습니다 :

> (Date.) ①  
;= #<CompilerException java.lang.IllegalArgumentException:  
;= Unable to resolve classname: Date, compiling:(NO_SOURCE_PATH:1)>  
(java.util.Date.) ②  
;= #<Date Mon Jul 18 12:31:38 EDT 2011>
(import 'java.util.Date 'java.text.SimpleDateFormat) ③  
;= java.text.SimpleDateFormat  
(.format (SimpleDateFormat. "MM/dd/yyyy") (Date.)) ④
;= "07/18/2011"

① **Date**는 **java.util** 패키지에 있고, 짧은 이름은 현재 네임스페이스에 포함되지 않았기 때문에 오류가 발생한다.
② 우리는 전혀 명시 적으로 가져 오지 않고 자바 클래스와 인터페이스를 사용할 수 있지만, 이러한 사용은 불편하게 자세한 정규화 된 클래스 이름을 필요로 합니다.
③ 당신은 클래스의 이름을 지정 기호로 **가져 오기**를 제공하여 현재 네임 스페이스에 클래스를 가져올 수 있습니다.
④ 반입 후, 클래스 '짧은 이름들을 참조하는데 사용될 수 있다.

**java.lang** 패키지의 모든 클래스들은 초기값으로 모든 네임스페이스들에 가져오기가 된다. 예를 들어, **java.lang.String**는 **String** 심볼을 통해 사용가능하고, 별도로 **import** 될 필요가 없다.

싱글 패키지로부터 다중 클래스들을 가져오길 원한다면, **require**로 같은 접두어로 네임스페이스를 위한 수용되는 package-prefixed collection 의 종류로 **import** 되어 제공될 수 있다.

> (import '(java.util Arrays Collections))  
;= java.util.Collections  
(->> (iterate inc 0)  
(take 5)  
into-array  
Arrays/asList  
Collections/max)  
;= 4

이는 드문경우이지만, 같은 네임스페이스로 같은 짧은 이름을 두 클래스를 가져올 수 없다.

> (import 'java.awt.List 'java.util.List)  
;= #<IllegalStateException java.lang.IllegalStateException:  
;= List already refers to: class java.awt.List in namespace: user>

해결 방법은 여기 (자바 등)는 네임 스페이스 내에서 가장 자주 사용하는 하나를 가져올의 다른 정규화 된 클래스 이름을 사용하는 것입니다.

***

Clojure의 **import**가 개념적으로 Java의 **import**구문과 유사하지만, 중요한 차이점이 몇 가지 있습니다.
먼저, **import java.util.*;.** 같은 Java의 와일드카드를 제공하지 않습니다. 싱글 패키지로 부터 다수의 클래스를 가져오려면, 위처럼 package-prefixed 부분으로 각각 열거되어야 합니다.
둘째, inner class(**java.lang.Thread.State, java.util.Map.Entry**)를 참조하려면 Java-internal notation 을 사용하여야 합니다.(e.g., **java.lang.Thread$State,java.util.Map$Entry**). 이것은 내부 클래스는 참조에 적용되지만, **import**에는 그렇지 않습니다.

***

**ns**는 선언적으로 최상위 문서, 이것이 무엇을 필요 대해 참조, 사용, 성공적으로 로드하고 제대로 작동하려면 가져온 필요와 함께 네임 스페이스의 이름을 지정할 수 있습니다. 그것은 이러한 기능의 주위에 매우 얇은 래퍼입니다; 그러므로 이 기능 함수의 pile은 호출합니다:

> (in-ns 'examples.ns)  
(clojure.core/refer 'clojure.core :exclude '[next replace remove])  
(require '(clojure [string :as string]  
[set :as set])  
'[clojure.java.shell :as sh])  
(use '(clojure zip xml))  
(import 'java.util.Date  
'java.text.SimpleDateFormat  
'(java.util.concurrent Executors  
LinkedBlockingQueue))

위는 **ns** 호출과 동일합니다.

> (ns examples.ns  
(:refer-clojure :exclude [next replace remove])  
(:require (clojure [string :as string]  
[set :as set])  
[clojure.java.shell :as sh])  
(:use (clojure zip xml))  
(:import java.util.Date  
java.text.SimpleDateFormat  
(java.util.concurrent Executors  
LinkedBlockingQueue)))  

**refer**, **require**(use 대신 :use 여기서 사용되는 키워드 주목)에 대한 모든 의미는, **ns**는 매크로이기 때문에, 광범위한 이름 인용은 불필요하다.

***
이전 예제에서 **clojure.core**에서 vars를 누락시켰다. 그들의 이름이(**next, replace, remove**) **clojure.zip**에 정의된 이름과 충돌하기 때문이다. 우리는 몇 줄 아래로 제외하지 않고 **사용**합니다. **clojure.zip**의 **use** **clojure.core**(주의를 갖는)로 부터 참조된 vars 에 매핑되는 재정의된 것이다. 여기에 명시적으로 이를 제외 분명히 우리가 충돌 알고 나중에 개선 할 수 있습니다.
***

보통 REPL에서 한번 정의하면, 네임스페이스는 런타임에 검사되고 수정된다. 399페이지 “The Bare REPL”에서 우리는 런타임에서 네임 스페이스 작업에 사용할 수있는 다른 도구에 대해 이야기할 것이다.

##### Namespaces and files

Clojure의 소스 파일을 구성해야하는 방법에 대한 몇 가지 하드 하고 빠른 규칙이 있습니다 :

**Use one file per namespace. 네임스페이스 하나에 파일 하나 사용** 각 네임 스페이스는 별도의 파일에 정의되어야하고, 프로젝트의 Clojure**의 소스 루트에서이 파일의 위치는 네임 스페이스의 세그먼트와 일치해야 합니다. 예를 들어, **com.mycompany.foo 네임스페이스는 com/mycompany/foo.clj에 위치해야 한다. 그 네임스페이스가 요구되거나 사용되어야 할 때, **(require 'com.mycompany.foo)** com/mycompany/foo.clj 는 로드되는데, 그 후 네임 스페이스를 정의해야 하거나 오류가 발생합니다.

**Use underscores in filenames when namespaces contain dashes. 대시가 포함된 네임스페이스는 파일 이름으로 언더 스코어(_)를 사용** 간단히, 네임스페이스가 **com.my-project.foo** 라면, com/my_project/foo.clj 에 소스코드가 위치한다. 오직 파일 이름과 네임 스페이스의 세그먼트에 해당하는 디렉토리 인해 변하지 있습니다. 당신은 선언된 이름을 사용하여 Clojure의 코드 네임 스페이스를 참조 계속 것입니다. **(require 'com.myproject.foo)** **(require 'com.my_project.foo)** JVM이 클래스 또는 패키지 이름에 대시를 허용하지 않기 때문에이 작업이 필요합니다. 그렇게 네임 스페이스, vars, locals 등을 포함 Clojure의 개체를 지정할 때 밑줄 대신 대시를 사용하는 것이 일반적으로 관용적이다.

**Start every namespace with a comprehensive ns form. 포괄적인 ns 폼으로 모든 네임스페이스를 시작** 모든 네임스페이스의 "최상위" (통상적으로) 파일에서의 첫번째 Clojure 폼은 잘 만들어진 **ns** 폼이어야 한다; **require** 와 **refer** 같은 네임스페이스-조작 함수의 사용이 없으면, REPL 환경이 불필요한 외부이다. 이외에도 단지 좋은 형태는, **ns** 사용이다:

1. 그렇지 않은 경우 **require** 사용 등으로 통합을 권장합니다.
2. 쉽게 독자와 코드 이후 테이너는 항상 각 파일의 상단에 위치하기 때문에 주어진 네임 스페이스 종속 관계가 있는지의 즉각적인 인상을 얻을 수 있도록합니다.
3. **ns** 는 평가되지 않은 이름을 허용 할 수있는 매크로이기 때문에, 리팩토링 필요한 네임 스페이스, 기능, import 클래스의 집합을 수정해야하는 다른 코드 조작 도구에 대한 문을 열어 둡니다. 낮은 수준의 네임 스페이스 수정 양식과 함께 가능한 무제한 평가는 도구가 불가능합니다.

**Avoid cyclic namespace dependencies. 순환 네임스페이스 의존 회피** 방향성 비순환 그래프를 형성해야하는 응용 프로그램 내에서 Clojure의 네임 스페이스 간의 종속성; 의미, 네임 스페이스 X는 자체가 (직접 또는 해당 종속성 중 하나를 통해) 네임 스페이스 X를 필요로하는 네임 스페이스 Y를 필요로 할 수 없습니다. 이 작업을 수행하려고 하면 이 같은 오류가 발생합니다.


>;= #<Exception java.lang.Exception:  
Cyclic load dependency:  

`［ /some/namespace/X ］->/some/namespace/Y->［ /some/namespace/X ］>`

**Use declare to enable forward references. 참조를 보내는 선언 사용** Clojure의 부하가 가는 대로 이전에 정의 된 var에 대한 참조를 해결 각 네임 스페이스의 파일을 연속적으로 각각의 형태. 이 정의되지 않은 var를 참조하면 오류가 발생한다 :

> (defn a [x] (+ constant (b x)))  
;= #<CompilerException java.lang.RuntimeException:  
;= Unable to resolve symbol: constant in this context, compiling:(NO_SOURCE_PATH:1)>

많은 언어들이 그들에 대한 참조를 해결하기 전에 프로그램 내에서 "매달려" 식별자를 모두 찾을 수 있도록 컴파일 단위를 정의; Clojure에서 이 작업을 수행하지 않습니다. 선명도 또는 스타일의 이익을 위해, 당신은 higherlevel을 정의하려는 경우, 모두 손실되지 않습니다. 낮은 수준의가 참조하는 것 전에 기능 : 사용은 현재 이름 공간에서 var는, (자유롭게 **declare**된 vars를 자유롭게 참조)하여 높은 수준의 함수를 정의하고 이전에만 **선언**했던 VAR를 정의 인턴을 선언합니다.

> (declare constant b)  
;= #'user/b  
(defn a [x] (+ constant (b x)))  
;= #'user/a  
(def constant 42)  
;= #'user/constant  
(defn b [y] (max y constant))  
;= #'user/b  
(a 100)  
;= 142

주의해야 할 하나의 주름은 당신이 실제로 이전에 선언 된 var를 정의하는 데 소홀히하는 경우, 그 var이 높은 수준의 코드를 수행하려고 할 때 거의 확실하게 예외가 발생합니다 런타임에 역 참조 사용할 수없는 자리 값을 얻을 것이다 그것으로 무엇인가.

**Avoid single-segment namespaces. 단일-세그먼트 네임스페이스 피하기** 네임 스페이스는 여러 세그먼트가 있어야합니다. 예를 들어, **com.my-project.foo** 네임스페이스는 세개의 세그먼트를 갖습니다. 이유는 두가지입니다.

1. 단일 세그먼트 AOT 네임 스페이스를 컴파일하는 경우, 그 처리는 기본 패키지에 있는 적어도 하나의 클래스 파일을 생성한다 (즉, "베어"클래스는 자바 패키지 아니다). 이는 일부 환경에 로드되는 네임 스페이스를 방지 할 수 있습니다, 그러므로 항상 기본 패키지의 클래스의 사용에 해당 언어의 제한으로 자바에서 사용할 수 있는 네임 스페이스의 해당 클래스를 방지할 수 있습니다.
2. 당신이 절대적으로있어 경우에도 신중한 것보다, 당신은 결코 당신의 단일 세그먼트 네임 스페이스에 대한 AOT 컴파일 된 클래스 파일을 재배포 할 것 없다하고 적극적으로해야합니다, 당신은 여전히 상관없이 당신에 얼마나 영리한, 네임 스페이스 충돌의 더 높은 위험을 실행하지 일을 이름.

우리가 세그먼트 깊이 네임 스페이스에 관해서는 부조리의 높이 도달 것을 권고하고 있다고 생각하지 마십시오. 누구도**com.foo.bar.baz.factory.factory.factories.Factory** 와 같은 이름을 좋아하지 않는다. 그와 단일 세그먼트 사이에 행복 중간이있다, **app** 또는 **util**과 같이 쉽게 충돌수 있는
당신이 어떻게 네임스페이스를 구성하는가에 상관없이, 그들은(그리고 당신의 라이브러리와 의존된 모든 다른 코드와 자원들)은 _clathpath_ 에서 로드될 것이다.

##### A classpath primer

자바에 익숙하지 않은 프로그래머의 경우, _classpath_는 종종 혼란의 원인이 될 수 있습니다. _classpath_는 사용자 정의 라이브러리와 리소스를 찾을 때 JVM이 사용하는 검색 경로입니다. 이 경로는 디렉토리와 _.ZIP_ 아카이브, _including.jar_ 파일을 모두 포함 할 수 있습니다. Clojure의이 JVM에서 호스팅되고, 그것은 자바의 클래스 경로 시스템을 상속합니다.
클래스 경로는 자신의 특질을 가지고 있지만 고유하지 않습니다. 당신은 입지 조건이 잘 알고있는 다른 검색 경로 메커니즘에 많은 유사점이있다. 예를들어, 유닉스와 윈도우 환경 모두에서 쉘은 실행 파일을 찾을 수 있습니다 경로의 연결된 세트를 저장하는 **PATH** 환경 변수를 정의합니다. 루비와 파이썬은 또한 검색 경로를 가지고 : 파이썬은 **PYTHONPATH** 환경 변수에 의존하며, 루비는 런타임 변수 **$LOAD_PATH**에 저장합니다. 이 모든 경우에, 검색 경로는 시스템 전체를 자동 설정하고 (Ruby Gems이나 파이썬 **easy_install**을 **PIP**과 같은) 의존성 관리 툴의 조합에 의해 처리되는 경향이있다.
클래스 패스의 같은 자동 구성은 Leiningen와 메이븐를 통해 사용할 수 있습니다. 대부분의 Clojure 프로젝트에서 가장 인기있는 자바 IDE와 이맥스에 사용되는 도구들은 종속성 관리에 이용된다. 예를 들어, 한번 당신의 _project.clj_ 또는 _pom.xml_ 파일에 종속성을 정의하면, 자동으로 REPL의 클래스 패스에 추가된다. 당신이 **lein-ring** or **jetty:run**을 통해 로컬 웹 응용 프로그램을 실행할 때와 같은 전체 응용 프로그램을 부트 스트랩 Leiningen이나 메이븐 플러그인을 사용하는 경우 동일하게 적용됩니다. 당신이 쉘에서 직접 자바 프로세스를 시작해야하는 경우에는 수동으로 클래스 경로를 구성 할 필요가 있다. 당신은 클래스 패스는 당신이 이해하는 데 도움이 될 것입니다 가장 기본적인 방법으로 정의하는 방법을 알고 명령 줄에서 Clojure를 사용하지 않을 경우에도 무엇보다 진보 된 도구는 당신을 위해 도움을 줍니다.

**Defining the classpath. 클래스패스 정의** 기본적으로 클래스 경로는 비어 있습니다. 이 모두가 기본적으로, 현재 작업 디렉토리(.)를 포함하며, 우리가 언급 한 다른 검색 경로 메커니즘에 비해 불편 차이입니다. 그래서 이 최상위 라이브러리는 런타임에서 찾을 수 있도록 합니다.
자바 프로세스에 대한 클래스 경로를 설정하려면, **-cp** 플래그를 명령 행에 지정합니다. 예를 들어, 현재 작업 디렉토리, _src_ 디렉토리, _Clojure.jar_와 유닉스 계열 시스템의 lib 디렉토리에있는 모든 _jar_ 파일을 포함하려면 다음과 같다.

`java -cp '.:src:clojure.jar:lib/*' clojure.main`

***
다른 모든 검색 경로 메커니즘과 마찬가지로, 클래스 패스 인해 다른 시스템에서 파일 이름 규칙의 차이로 플랫폼에 의존적으로 정의된다. 유닉스 계열 시스템에서 클래스 경로는 다음과 같습니다 :-delimited 목록 /-defined paths; Windows에서, ;-delimited 목록 /-defined paths. 그래서, 유닉스 계열 시스템에 대한 위의 예 클래스 경로는 Windows에서는 아래와 같다 :

`'.;src;clojure.jar;lib\*'`
***

**Classpath and the REPL. 클래스패스와 REPL** 클래스 경로는 런타임에 Clojure에서 확인 할 수 있습니다 :

> $ java -cp clojure.jar clojure.main  
Clojure 1.3.0  
(System/getProperty "java.class.path")  
;= "clojure.jar"

JVM 프로세스가 명령 줄 매개 변수 또는 환경 변수를 통해 시작할 때 (**java.class.path** 시스템 프로퍼티에 의해) 기본 클래스 경로가 정의됩니다. 하지만 불행히도 런타임에 변경할 수 없다. 이것은 지속적인 REPL 세션을 열고 열어두고 참여하는 경향이 Clojure에서의 일반적인 개발주기와 확률에 있습니다. 클래스 경로에 대한 변경 사항은 따라서 JVM을 다시 시작하고, REPL을 다시 시작해야합니다.

#### Location, Location, Location

Clojure의 프로젝트에 사용되는 두 개의 주된 프로젝트 레이아웃 규칙, Clojure의 프로젝트에서 사용하는 주된 빌드 도구에 의해 정의되는 기본값이 있습니다. 첫째, "메이븐 스타일". 프로젝트 내에서 언어와 역할에 따라 별도의 서브 디렉토리에 소스 파일을 최상위 _src_ 디렉토리 아래의 모든 소스 파일을 분리해 저장한다. 공개 API 또는 제공 기능을 정의하고 기본 소스 코드는 _src/
main_에 저장된다; 일반적으로 배포되지 않는 장치와 기능 테스트를 정의 코드는 _src/test_에 저장된다.

> Example 8-1. The “Maven-style” project layout  
＜project dir＞  
|  
|- src  
　　|- main  
　　|- clojure  
　　|- java  
　　|- resources  
　　|- ...  
　|- test  
　　|- clojure  
　　|- java  
　　|- resources  
　　|- ...  

프로젝트 레이아웃이 정렬을 사용하면, 클로저 소스 파일들은 _src/main/clojure_에 위치하고, 자바 소스 파일들은 src/main/java가 최상위 폴더이다. 역할과 파일 형식이 디렉토리 구조에 반영되어 있다는 사실은 일부 활동을 간단 할 수 있습니다. 예를 들어, 오히려 소스 루트에서 특정 유형의 파일 세트를 선택하는 파일명 필터를 사용하는 것보다, 당신은 "맹목적으로"파일의 각 유형은 루트 디렉토리를 참조함으로써 파일의 세트를 참조 할 수 있다. 이것은 크게 자원의 패키징을 단순화 할 수 있다 : 사용자가 웹 애플리케이션에 포함되어야 할 필요 자원의 세트가 있는 경우 (이미지, 자바 스크립트 파일 등), _src/main/webapp_ 하위에 그룹화 할수 있다. 안전하게 당신이 당신의 빌드 및 패키징 공정에 의해 참조되지 않았다고 확신 할 수 있는 다른 소스 루트 재배포하여 자원을 넣을 수 있습니다. 메이븐 스타일의 레이아웃은 가장 표준화 된 옵션입니다. - 소스 파일 위치 규칙의 그 격려는 그것을 사용하는 프로젝트는 거의 벗어나지 않는다는 것을 의미한다. 메이븐 스타일의 프로젝트 레이아웃의 주요 단점은 파일 경로를 추가하는 _src/main_, _src/test_ 설정에 더 기인한다는 것이다. 다른 주된 프로젝트 레이아웃 스타일은 프로젝트에서 프로젝트에 실질적으로 다를 수 있기 때문에, 특성화하기 어려운 하나입니다 :

> Example 8-2. The “freeform” project layout examples  
＜project dir＞  
|  
|- src  
|- test  
＜project dir＞  
|  
|- src  
　|- java  
　|- clojure  
|- test  
|- resources  
|- web

메이븐 스타일의 레이아웃에 비해 자유 프로젝트 레이아웃은 짧은 파일 최적화 경로 길이 (일부는 명령 행에서 파일을 참조하는 것을 쉽게하기 위해), 일반적으로 _src_ 및 _test_의 존재를 외부 프로젝트에서 프로젝트 재사용 적은 수의 규칙이 있다.
때때로 특정 프로젝트의 빌드 구성에 따라 아니지만 다른 종류의 소스 파일은 종종, (예를 들어, 자바와 Clojure의 두 소스 파일이 src 루트가 될 수 있습니다) 같은 소스 루트 내에서 혼합된다. 당신은 프로젝트 Leiningen 포함 메이븐 이외의 빌드 도구와 함께이 레이아웃을 사용하는 것을 확인할 수 있습니다.

#### The Functional Organization of Clojure Codebases

지금까지, 우리는 기본적인 기본 규칙에 대해 이야기했습니다. 파일이 저장되는 곳, 명명 규칙, 네임 스페이스와 파일 사이의 대응 등. 더 미묘한 기능적 관점에서 Clojure의 코드를 구성하는 방법에 대한 질문은 다음과 같습니다 :
* 얼마나 많은 기능이 특정의 알고리즘을 구현하는 데 사용되어야 하는가?
* 네임 스페이스는 얼마나 많은 기능을 포함해야합니까?
* 이 프로젝트는 얼마나 많은 네임 스페이스를 포함해야합니까?

명시적으로 정의 결국 종종 "좋은 스타일."을 정의하는 특정 요구 사항이 있기 때문에 다른 프로그래밍 언어에 관련된 추론 문제는 부분적으로 대답하는 것이 더 쉽다. 다양한 언어 많은 자주 사용되는 프레임 워크를 정의하는 방법 플러그인 / 구성 / 모델 / 확장 구체적 기대를 가지고 (예를 들어, "데이터베이스 테이블 당 하나의 클래스"또는 "사용자 인터페이스 컴포넌트 당 하나의 모듈")되도록 형상 코드베이스가 사용하는 라이브러리와 배포 될 광범위한 환경의 부수적 또는 기계적 특성에 의해 상당 부분 결정된다.
대조적으로, 만약 거의 특정 라이브러리 또는 프레임 워크를 사용하여 부수작용으로 Clojure의 애플리케이션의 특정 조직 고수하도록 강요되지 않는다. 특히, 함수형 프로그래밍 기법 및 매크로 가끔, 현명한 사용의 광범위한 응용 프로그램을 사용하면 다른 언어로도 훨씬 가능한 도메인의 윤곽을 거울 Clojure의 라이브러리와 응용 프로그램을 구성 할 수 있습니다. Clojure의 당신이 당신의 데이터 모델은 종종 자연적으로 당신이 생각했던 것보다 더 많은 프로그램의 구조가 달라집니다. 최종 결과와 당신보다 당신의 도메인에 대해 더 명확하게 생각하는 것을 권장합니다.
즉, 아주 일반적인 원칙의 외부, 아마 Clojure의 프로그램의 "전형적인 구조"같은 건 없다, 모두가 말을하는 것입니다. 이 개념은 어느 당신의 배경과 기대에 따라, 당황하거나 매우 매력적일 수 있다. 우리의 경우, 우리는 멀리 문제 에서 오래 전에 내린 결정에 의해 부과 된 순서의 특정 종류와 함께 할 수 산만하지 않고, 기능 또는 알고리즘 또는 도메인의 본질에 초점을 맞출 수 있는 것은 이 지속적으로 상쾌한 발견했습니다. 우리는 우리의 코드에서 해결하는 것을 목표로 하고 있습니다.

##### Basic project organization principles

우리가 막연하게 일반 원칙에 대해 이야기하고 우리가 생각하고있는 것을 최소한 몇을 언급하지 않는 것은 잘못된 형식이 될 것이다.
* 아마 다른 네임 스페이스에서, 별도의 다른 일을 하십시오. 고객 레코드와 함께 작동 코드가 있는 네임스페이스는 멀리 웹 콘텐츠에 대한 템플릿을 로드하는 네임 스페이스에 있어야합니다.
* 함께 관련 일을 계속, 어쩌면 네임 스페이스로 명시되어 자연 범주로 분류. 예를 들면, 높은 레벨 API (예를 들어, **foo.ui**) 및 하위 레벨 또는 공급자의 API (예 **foo.ui.linux**과 **foo.ui.windows**) 간의 관계 등을 나타 내기 위해 스페이스의 이름에 의해 암시 계층을 사용.
* 구현 고유의 데이터 나 기능을 포함 var 정의 **^:private** (또는 개인의 기능을 정의하기위한 **defn-** 편리 양식 사용, 개인) 가능한 한 많이. 이것은 여전히 그들에게 var 특수 형태 (또는 reader sugar # ')를 통해 기능과 데이터의 경우 절대적으로 필요 "커튼 뒤에"에 액세스 할 수있 는 뒷문을 제공하면서 무의식적으로, 변경 될 것들에 따라에서 클라이언트를 유지합니다.
* 자신을 반복하지 마십시오 지정된 네임 스페이스에서 한 번만 상수를 정의하고 보증 된대로 유틸리티 기능 및 유틸리티 네임 스페이스에 공통 기능을 휴식.
* 당신이 할 수 있을 때, 대신 그 중 하나의 특정 구체적인 구현 결합보다, 공통의 참조 유형, 컬렉션의 _추상화_ 및 시퀀스를 사용합니다.
* Unpure 기능은 유해한 것으로 간주하고, 꼭 필요한 경우에만 실행되어야한다.

큰 관점에서, 귀하의 Clojure의 프로젝트는 동일한 관심에서 모듈과 다른 언어로 작성된 프로젝트의 혜택을 얻을 수의 분리에 도움이 됩니다. 그건 그렇고, 네임 스페이스는 사용자의 이익을 위해 단독으로 제공하는 조직의 도구라는 것을 기억하자. 500개의 네임 스페이스를 사용하여 작성된 큰 응용프로그램은 하나의 거대한 네임스페이스에 넣은 것 과 같이 기능하고 수행된다. 따라서, 도메인의 구조에 맞고, 팀의 방식에 맞게 응용 프로그램을 구성해야합니다.
