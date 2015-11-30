# Build

"빌드"는 코드가 전달되었지만, 코드를 쓴 _후_ 수행한 것들을 모두 다 포함하는 것을 총칭(umbrella term)이다.(다른 용어, 서비스로서의 소프트웨어 같은 클라우드 컴퓨팅 등과 같이)

우리의 목적은 여기에 있다. _build_의 의미를 생각해 본다.
* 컴파일
* _의존성 관리_, 외부자원들을 체계적으로 허용하기
* 컴파일 결과와 다른 프로젝트 자원을 _산출물_에 패키징 하기
* 의존성 관리 컨텍스트에서 산출물 배포
즉, 지나치게 형식적인 설명은 활동보다 더 복잡하다.
이미 당신은 이미 이런 것들을 사용하고 있다.

> Table 8-1. Contrasting “build” solutions for different programming languages
책의 표

Clojure는 JVM 언어이므로 자연스럽게 큰 생태계의 빌드, 패키징, 배포 인프라와 기술의 큰 흐름(길)을 재사용한다.

* 훨씬 더 멋진 "UI"와 Clojure의 네이티브 개발 환경을 제공하면서 Leiningen는 메이븐 인프라의 상당 부분을 다시 사용합니다.
* 클로저를 빌드하는 목적을 둔, 메이븐, 그래들, 앤트와 같은 플러그인들이 있다.
* Clojure의 라이브러리가 _.jar_ 파일로 패키징되어, Clojure의 웹 응용 프로그램은 (보통) _.war_ 파일 등으로 패키징됩니다.
* Clojure의 라이브러리는 모든 자바 (따라서 Clojure의) 빌드 도구에 액세스 할 수 있습니다 메이븐 저장소를 통해 배포됩니다.

Clojure의 자바의 빌드 툴과 관행 사이의 정렬은 Clojure의 응용 프로그램 및 라이브러리에 의존하고 자바 라이브러리를 사용할 수 있습니다, 그리고 당신이 그 Clojure에 작성된 라이브러리를 배포 할 수 있습니다. 의존하여 사용가능한 다른 JVM 언어(자바, 그루비, 스칼라, JRuby를, 자이썬과 등과 같은)를 사용한 프로그래머들에게 클로저로 쓰이고 배포된 라이브러리를 사용하도록 허용한다.
당신은 이미 Java 또는 다른 JVM 언어를 사용하는 경우, 당신은 당신의 코드베이스에 일부 Clojure의 추가는 기존 빌드 프로세스에 거의 영향을 미치지 않는다는 것을 발견할 것이다. 반면에, 루비, 파이썬, 또는 다른 비 JVM 언어에서 오는 경우, 당신은 다음 사실에 안도할 것이다. Clojure 빌드 프로세스와 설정은 자바 맞춤형 필연적(Java-tailored corollaries)보다 훨씬 간단하다는 사실이다.

## Ahead-of-Time Compilation
우리는 3 페이지의 "Clojure의 REPL"에서 언급 한 바와 같이, Clojure의 코드는 _항상_ 컴파일됩니다--Clojure의 인터프리터는 없습니다. Clojure의 코드에 대해 지정된 청크의 바이트 코드를 생성하고 호스트 JVM으로 그 바이트 코드를 로드하는 것을 포함해 컴파일은 두 가지 방식으로 일어날 수 있다 :

* 런타임시; 이것은 당신이 REPL, 또는 때 디스크에서 Clojure의 소스 파일을 로드를 사용할 때 발생하는 것입니다. 소스 파일의 내용은, 바이트 코드로 컴파일 및 JVM으로 로드된다. 이 바이트 코드와 그것을 정의하는 클래스는 JVM이 종료 된 호스트 후 유지되지 않습니다.
* "Ahead-of-Time Compilation"(AOT) 컴파일이 런타임 컴파일과 동일하지만, 바이트 코드는 JVM 얻어진 클래스 파일로서 디스크에 저장된다. 그러한 클래스 파일은 다음 원래 Clojure 원본 파일 대신 나중에 JVM 인스턴스에서 재사용 될 수 있다.

> Figure 8-1. Clojure’s compilation process

책의 그림 확인

Clojure의 코드가 어떤 기능 차이없이, 소스 파일 또는 교환 AOT 컴파일 된 클래스 파일에서로드 할 수 있습니다. 네임 스페이스를 require(예를 들어 **(require 'clojure.set)**)하는 것은 해당 네임 스페이스를 정의하거나 해당 AOT 컴파일 된 클래스 파일에서 **Clojure/set.clj**소스 파일에 대한 클래스 경로를 검색합니다.
따라서, 몇 가지 선택 상황의 외부, AOT 컴파일은 전적으로 선택 사항입니다.
Clojure의 라이브러리와 응용 프로그램은 일반적으로 AOT 컴파일을 요구하는 기술적인 이유가 없기 때문에, 이 때 당신이 할 수 있을 때 Clojure 소스 배포 우선이 합리적이다. 사실, AOT 컴파일은 몇 가지 중요한 단점이 있을 수 있습니다.

1. JVM 클래스 파일들이 생성되는 Clojure의 원본 파일에 비해 훨씬 더 큰 디스크에 의해 정의된다.
2. AOT 컴파일 다시 다른 경량 Clojure의 개발주기에 별도의 컴파일 단계를 추가합니다.
3. AOT로 컴파일하는 프로젝트는 그 소스들의 AOT 컴파일로 사용된 클로저의 버전으로 클로저 라이브러리나 애플리케이션을 고정합니다. 당신은 클로저의 다른 버전 런타임을 디플로이 할 수 있는 버전의 AOT 컴파일 코드를 추정할 수 없습니다.
4. AOT 컴파일은 _전이_이다. 당신이 네임 스페이스 **bar**를 필요 네임 스페이스 **foo**는 컴파일 AOT 경우, 다음 줄은 AOT  컴파일되어 진행된다. 당신이 당신의 빌드 구성을 주어진 예상 것보다 라이브러리나 응용 프로그램 내 특정 공간 의존성에 따라, 이 AOT 컴파일의 훨씬 더 큰 범위에서 발생할 수 있습니다.
***
당신이 필요로 하는 모든이 하나의 이름이 클래스 (또는 소수의) 자바 프레임워크에서 진입 점을 생성하는 경우, 전이성을 파괴하는 쉬운 방법이 있다 : **gen-class**(or the **:gen-class** option of ns) :impl-ns에 의해 명세된 명확한 구현을 사용. 편집 때문에 전체 코드베이스의 편집을 트리거하지 않습니다 **gen-class** 네임 스페이스의 편집을 필요로 하는, 이러한 의존성을 따르지 않습니다.


AOT 컴파일은 몇 가지 활용 상황을 준비해야 한다 :
1. 소스 코드를 배포하지 못하거나, 않으려 할 때.
2. 난독 같은 패키지 과정의 일환으로 다양한 클래스 파일 도구를 사용 할 때.
3. AOT-컴파일의 비용 부담으로, 응용 프로그램의 시작 시간이 _매우 중요_할 때. AOT 컴파일 클래스 파일들을 로드하면 Clojure의 소스 파일에서 동일한 지점에 도착하는 것보다 훨씬 빠르다.
4. 사용자가 Clojure의 코드가 **gen-class, defrecord, deftype, 또는 defprotocol**에 의해 생성하는 이름의 자바 클래스나 인터페이스를 통해 Java 또는 다른 JVM 언어에서 사용하려할 때.

우리는 349 페이지의 "AOT 컴파일 구성"에 Leiningen과 메이븐에 AOT 컴파일을 수행하기 위한 몇 가지 옵션에 대해 이야기한다.

## Dependency Management

모든 소프트웨어 개발 팀은 프로젝트에 대한 의존성 관리를 사용해야 합니다. Clojure의를 사용하는 팀은 다르지 않습니다. 멀지않은 때, 수동으로 주의깊게 (자주 lib 디렉토리) 프로젝트의 특정 장소나 버전 제어 시스템에 결과를 추가하는 _.zip_ 및 _.tar.gz_를 파일에 인터넷의 주위에 의존성을 제공하려할 때는 암흑기였다. 빌드 프로세스는 단순 파일 경로를 사용하여 이러한 의존성을 참조할 것이고, 그들은 배포 바이너리 또는 다른 산출물에 의존 번들 또는 부주의 프로젝트의 다운 스트림 사용자가 이미 그 의존의 동일한 버전을 할 것이다, 또는 어디에 알려진 것 중 하나를 얻을 수 있다.
현대의 요구는 고맙게도 수동 미세 조정하고 반복 가능한 프로세스의 완전한 부족에 대한 의존도 주어, 많은 부분에서 이러한 관행을 추진해 왔습니다.
펄은 CPAN (모든 의존성 관리 방식의 아마 할아버지)를 가지고 있지만, 파이썬은 핍과 **virtualenv**을 가지고 있으며, 루비는 **gem**과 **rvm**, JVM에 대한 의존성 관리 메이븐하고 2000 년대 중반에 정의 된 모델에 의해 주도되고 있다. Clojure의 완전히 당신이 사용할 수있는 도구를 구축 관계없이 모델을 포함한다.

## The Maven Dependency Management Model

메이븐의 의존성 관리 모델은 다음 기능이 있다:
* _좌표_를 사용하여 _산출물_의 식별 및 버전관리
* 산출물의 의존성 라이브러리와 그것을 생산한 프로젝트 선언
* 의존성 라이브러리 사양에 따라 _저장소_에서 산출물의 저장 및 검색
* 산출물 의존성 라이브러리 전이 계산

이들 개념과 관련된 시스템의 다양한 원리를 살펴 보자.

### Artifacts and coordinates

산출물이란 프로젝트 빌드 프로세스에서의 제품으로 어떤 파일이다. Clojure의 라이브러리와 응용 프로그램은 단지 자바와 다른 JVM 언어를 사용하여 작성된 것과 같이 패키징됩니다. 구체적으로는, 이것은 당신이 주로 산출물의 두 가지 유형을 사용하고 생성 할 수 있다:
* _.jar_ 파일들로, 다음이 포함된 _.zip_ 파일들:
-- 그들의 소스 디렉토리 최상단 또는 컴파일 대상 디렉토리에서의 위치를 미러링 Clojure의 소스 파일, JVM 클래스 파일, 및 계층 구조에서 (정적 구성 파일, 이미지 등) 다른 자산
-- **META-INF** 디렉터리 리스트의 선택적인 메타데이터
* _.war_, 또한 _.zip_ 들로 웹 애플리케이션을 패키징한 JVM 표준이다. 560페이지 “Web Application Packaging” 을 보면, _.war_ 파일과 용법을 알 수 있다.

***
**Rare Packaging Types**
일반, 꾸밈 _.zip_ 또는 _.tar.gz_를 파일도 메이븐 저장소에 가끔 발견되지만, 이들은 더 자주 비 코드 자산을 전송하는 데 사용된다. 예를 들어, 빌드시 자동화 된 방식으로 구성되어 클라이언트 측 응용 프로그램 설치와 JVM 설치 프로그램을 포함 할 수 있습니다; JVM 설치 프로그램을 압축하는 조직의 메이븐 저장소에 이를 배포하는 프로젝트가 스크립트에 그 설치의 포함을 종속성을 선언 할 수 있도록 합니다. 그들은 _.jar_ 파일 테마에 크게 다만 변화가 있습니다 (존재하는 경우 적) 특정 프레임 워크의 외부에서 사용이 거의 없지만 또한, 이클립스와 넷빈즈 리치 클라이언트 플랫폼 같은 애플리케이션 프레임 워크는 자신의 포장 유형과 요구 사항을 가지고 있는 그들 위한 것입니다.
***

모든 유형의 산출물은, 함께 고유 산출물의 특정 버전을 식별하는 속성의 묶음은 _좌표_를 사용하여 식별됩니다.
**groupId**, 종종 org.apache.lucene, com.google.collections와 같은 조직이거나 프로젝트 식별자.
**artifactId**, lucene-core, lucene-queryparser 같이 조직 또는 프로젝트 내의 산출물의 식별자.
조직과 프로젝트 사이에 차이가없는 경우 작은 Clojure의 오픈 소스 라이브러리가 동일한의 groupId와 artifactId를 가하는 것이 일반적인 비록 프로젝트는 종종 groupID가 같아서 여러 관련 이슈를 생산하고 있습니다.
**packaging**, 산출물 자체의 파일 확장에 대응되는 것을 참조하는 산출물의 형태 식별자
기본은 **jar** 이고, 기본 형태에서 정의되지 않음.
**version**, 의미적으로 버전 관리된 컨벤션을 따르는 이상적인 버전 문자열.

텍스트 설정에서, 메이븐은 groupId:artifactId:packaging:version 과 같은 형태로 좌표를 정의한다. 그래서 클로저 jar의 v1.3.0 은 org.clojure:clojure:1.3.0 을 참조한다(jar 패키징이 기본이라는 것을 기억하자). 각 프로젝트는 당신이 라이닝겐을 사용한다면, _project.clj_에 자신의 좌표를 정의한다. - 때때로 메이븐은 _pom.xml_에 - 어쨌든, 프로젝트는 도출된 산출물을 배포하기 위해 만들어질 때마다, 대응하는 pom.xml 파일이 메이븐 _저장소_로 산출물과 함께 업로드 된다.

### Repositories

산출물의 세트가 메이븐 _저장소_에 업로드되면, 그 저장소는 일반적으로 인덱스 버전과 산출물과 함께 그것을 제공하는 pom.xml에 의존성 정보를 제공한다; 이를 _배포_라고 합니다. 그 시점에서, 저장소는 그들 (그 프로젝트 산출물에 의존해 거의 항상 다른 개발자)을 획득하고자하는 고객에게 산출물을 배포할 수 있습니다.
수백 전세계 조작 공공 저장소 (아마도 수천)가 있지만 메이븐 산출물의 대부분을 보유한 몇 개의 큰 저장소가 있습니다.

* _Maven central_, 메이븐 기반의 빌드 도구가 의존성을 검색 할 가장 큰 저장소 및 기본 위치. 모든 공식 Clojure의 분포 및 핵심 라이브러리는 중앙 메이븐에 배포됩니다.
* _Clojars.org_, Leiningen은 단순화 된 통합을 제공하는 오픈 Clojure의 커뮤니티 저장소. 많은 매우 인기있는 오픈 소스 Clojure의 라이브러리는 Ring, Clutch 및 Enlive 포함, Clojars에 배포됩니다.
* 아파치, 제이보스, 레드햇과 같은 대형 오픈 소스 단체뿐만 아니라 자신의 메이븐 저장소를 유지한다.

당신은 또한 어떤 점에서 메이븐 저장소의 두 가지 다른 형태와 함께 작동합니다 :
* 개인 / 내부 저장소는 자주, 그리고 때로는 메이븐 중앙 또는 Clojars 같은 프록시 공용 저장소에 자신의 프로젝트에 의해 생성 된 집의 산출물로 기업과 다른 조직에 의해 유지된다.
* _local repository_는 메이븐과 _~/.m2/repository_ 의 메이븐 기반 빌드 툴에서 만들어진다. 프로젝트의 의존성을 다운로드하고 나중에 사용하기 위해 캐시되는 곳이다. 당신은 또한 (개념적으로 배포와 같은 프로세스, 그러나 그래서 로컬 저장소를 대상으로되는 설치 작업을 구분하기 위한 이름)이 로컬 저장소에 빌드 프로젝트의 산출물를 설치하도록 선택할 수 있습니다.


### Dependencies

모든 프로젝트가 좌표를 정의하는 것처럼, 모든 프로젝트는 _의존성_을 정의합니다. 의존성은 다른 프로젝트의 산출물, 산출물을 사용하는 좌표를 참조하도록 표현됩니다. 의존성의 면세를 사용하는 것은, 메이븐 및 메이븐 기반 도구가 다음을 할 수 있습니다:

프로젝트의 _전이 종속성_ 세트 결정 - 즉, 프로젝트의 의존성의 의존성의 의존성이다. 더 공식적으로 말하자면 프로젝트의 의존성은 프로젝트 루트 방향성 비순환 그래프, 그것은 참조 사이클을 형성하는 프로젝트의 종속성 치명적인 오류가 등을 검토, 실행, 건설되고 형성한다.
프로젝트의 전이 의존성들 새로운 REPL들 및 컴파일 및 응용 프로그램 프로세스 JVM의 클래스 경로에 추가 종속성 '산출물로 시작할 수의 전체 집합을 감안할 때, 이 해당 프로세스에서 실행되는 코드가 그 의존성에 포함 된 클래스, 자원, Clojure의 소스 파일을 참조 할 수 있습니다.
프로젝트의 종속성을 해결하면 일반적으로 인식하지 않아도 간단한 그래프 탐색이다. 단지 주의해야 할 점은 주로 스냅 샷 및 버전 범위의 형태로 제공 의존성 버전을 지정에 존재하는 유연성이다.

***
**Clojure Is “Just” Another Dependency**

파이썬, 루비, 자신의 전용 런타임이 다른 언어에 경험이있는 사람들은 종종 당신이 루비의 일부 특정 버전을 설치할 수있는 것처럼, Clojure "설치"에 대한 기대가 있다. 이것은 그렇지 않다.
 기술적으로, Clojure의는 또 다른 자바 / JVM 라이브러리이며, 따라서 프로젝트 내에서 또 다른 의존성 라이브러리이다. 당신이 설치해야 하는 JVM이다. Clojure는 일반적으로 개발 및 테스트 동안 Leiningen 또는 메이븐의 도움으로, 응용 프로그램의 클래스 경로에 추가해야합니다.
사실은 Clojure가 JVM 라이브러리 구축 및 팀 및 고객 수용의 측면에서 매우 실질적인 혜택을 가질 수 있습니다.
***

**Snapshot and release versions.** 메이븐 의존성 모델에서, 버전은 _snapshots_이나 _releases_로 구분된다. 대부분의 버전 문자열-**1.0.0, 3.6, 0.2.1-beta5 와 같은**-은 배포 버전으로 생각된다. 당시에 고정되고 절대 변하지 않는 것을 의미한다. 이 업데이트되는 릴리스 버전 문자열 이전에 배포 한 이슈를 방지 할 수 있는 모든 정식 저장소에 의해 적용됩니다. 이 반복의 목적 빌드 지원 : 당신이 구축하고 종속성의 특정 버전에 대해 테스트 일단 그들이, 저장소에 배포 된 이후에 출시 된 산출물은 변경할 수 없기 때문에, 당신은 영원히 그 결과에 의존할 수 있습니다.
스냅 샷 버전은 완전히 다릅니다. **-SNAPSHOT** suffix—such as **1.0.0-SNAPSHOT**, 3.6-SNAPSHOT, 0.2.1-beta5-SNAPSHOT** 으로 끝나는 버전 문자열들은 스냅 샷은 개발의 최신 출물을 확인하기 위한 것을 나타낸다. 개발 자료가 생성되고 저장소에 배포 될 때 따라서, 같은 버전 번호, 시간이 지남에 따라 다른 구체적인 산출물을 참조 할 수 있습니다.
예를 들어, 버전에 이르기까지의 지속적인 발전을 추적하고 싶은 말은 ** 2.0.0 ** 특정 라이브러리 때문에 ** 2.0.0 ** 몇 가지 주요 기능을 프로젝트 요구 사항을 제공 할 것입니다. 가능성이 많은 배포됩니다 라이브러리 2.0.0 - 스냅 샷 버전의 빌드, 그리고 당신이 프로젝트의 종속성에 해당 버전 문자열을 지정, 당신은 항상 함께 최신 시험판 버전에 대한 테스트 작업을한다. 그런 다음 그 저자가 자신의 작업을 완료하고 프로젝트 저장소에 최종 릴리스 이슈를 배포하면 라이브러리의 2.0.0 릴리스 버전을 사용하여 전환 할 수 있습니다.

**Version ranges.** 당신이 라이브러리의 버전 ** 1.6.0 **에 의존성을 가진다고 선언하지만 당신은 당신이 라이브러리는 다음 주요 릴리스 될 때까지 API 호환성을 파괴하지 않고 안전하게 의존한다는 것을 알 수 있다. 거의 모든 이슈가 이 경우에서와 같이, 의미론적 버전을 사용하는 한, 이는 버전들의 범위 측면에서 의존성 버전을 정의하는 것이 유용하다. Maven은 버전 범위 포맷을 지원한다 :
> Table 8-2. Maven version range formats

Range format Semantics
(,1.0] x <= 1.0
1.0 “Soft” requirement on 1.0
[1.0] Hard requirement on 1.0
[1.2,1.3] 1.2 <= x <= 1.3
[1.0,2.0) 1.0 <= x < 2.0
[1.5,) x >= 1.5
a Taken from http://docs.codehaus.org/display/MAVEN/Dependency+Mediation+and+Conflict+Resolution.

그래서, 우리가 라이브러리에 **[1.6.0,2.0.0)**로 의존성을 지정할 수 있습니다. 우리의 프로젝트의 산출물이 **1.6.0**(포함)과 **2.0.0** (제외) 사이의 라이브러리의 모든 버전과 함께 배포 될 수 있습니다. 일단 **2.0.0**이 배포되면(아마도 주 버전 번호에 범프 주어진 이전 **1.x.x** 대가로 출시 호환성을 깨는) **2.0.0**과 새로운 릴리즈 배포와 작동하는 프로젝트를 (어쩌면 조정할) 프로젝트로 테스트하는게 필요하다 -- 아마도 **[2.0.0, 3.0.0)**의 라이브러리에 버전 범위 의존성을 지정하여 배포 할 수도 있습니다.
***
위의 표에서 "베어"버전 번호를 그 주 (예를 들면, 1.0)는 "소프트"버전 요건으로 명시된다. 이것은 특정 이슈의 두 개의 서로 다른 베어 버전에 -- 예를 들어 의존하는 경우, 당신은 라이브러리의 버전 1.2에 의존한다는 것을 의미하고 의존성 중 하나를 선택하여 사용되는 동일한 라이브러리 버전의 버전 1.6에 따라 달라집니다 모든 등 등, 빌드 REPLs합니다. 이는 문제가 야기되지 않지만 그렇게 되면, 버전 범위 지정 모호성을 제거한다. 거의 절대. 이 예에서, 당신은 [1.2,1.5]로 라이브러리에 직접적인 의존성을 변경하는 경우, 다음 버전 1.5가 선택됩니다.
***
이 시나리오와 일반 버전 범위의 사용량은에 의존 라이브러리를 생산하는 프로젝트의 당신의 평가에 따라 달라집니다. 그들은 (어떤 종류의) 의미 버전을 사용하는 경우 신뢰성, 다음 버전의 범위는 생산 산출물은 그들 만이 호환 될 가능성이 있는 의존성 버전과 함께 사용 될 수 있도록 할 수있는 좋은 방법이 될 수 있습니다. 의존성은 다른 방식을 사용하는 경우 버전 한편, 다음 버전 범위가 덜 유용 할 수있다.

## Build Tools and Configuration Patterns

이제 우리는 프로젝트 조직에 대한 몇 가지 키 배경과 개념을 덮고, Clojure 커뮤니티, Leiningen와 메이븐에서 가장 인기 있는 두 빌드 도구로 작업의 몇 가지 예를 살펴 보자. 이것은 비록 상당히 개괄적으로 생각되므로, 고려해야한다.
***
이 도구들은 각각의 방법에서 긴밀히 사용되고, 호환과 실패에서 이해되는 것이 아니다. 당신이 선택 도구를 구축하든, 그것은 제공하는 무엇을 최대한 활용할 수 설명서 및 지역 사회 자원을 참조하십시오. 마지막으로, 우리는 17장에서 웹 응용 프로그램을 위해 특별히 몇 가지 추가 빌드 예제를 제공 할 것입니다.
***
**Stick with what works.** 우리는 이 장의 시작 부분에서 말했듯이 도구를 구축에 관해서 특히 우리가 어떤 절대 아마 진리를-제공하는 것은 불가능하다. 그것은 우리가 Leiningen와 메이븐에 섹션에서 아래 길이에서 논의하여 Clojure 프로젝트에 사용할 도구를 결정하는 데 도움이 몇 가지 추론이 존재했다. 아마 그러나, 다른 사람을 오버라이드 (override) 할 필요가 하나 발견이 있다 :
조직이 주요 JVM 기반의 빌드 툴 체인 표준화 경우, 그것을 계속 사용. Clojure ant, 메이븐, Gradle, Buildr의 플러그인, 우리가 익숙하지 않은 아마 다른 시스템은 아직 이러한 도구를 사용하여 프로젝트에 Clojure 도입, 거기는 간단한 문제이며 빌드 및 프로젝트 관리 프로세스를 방해 필요하지 않습니다 그 장소와 잘 작동 이미 사용 중입니다.

### Maven

Maven은 아마 자바 세계에서 가장 일반적으로 사용되는 빌드 도구입니다.오픈 소스 아파치 프로젝트는, 범위는 대부분의 빌드 도구보다 훨씬 더 넓다 : 광범위한 타사 플러그인 커뮤니티를 통해, 그것은 통합과 같은 것들을 포함하는 소프트웨어 프로젝트의 "전체 수명주기를"관리하는 방법을 제공하는 것을 목표로 코드 컴파일 및 포장의 일반적인 빌드 프로세스에 추가 기능 테스트, 코드 커버리지, 테스트 보고 및 릴리스를 관리한다.
메이븐의 구별되는 특징 중 하나는 다른 JVM 기반 도구는 이클립스, 넷빈즈, 그리고 인 IntelliJ 같은 IDE를 같이 메이븐과 우수한 통합을 제공 찾을 것입니다; 허드슨 / 젠킨스, 클로버, 및 Cobertura 같은 팀 시설; 및 NSIS, IzPack, JavaCC에, ANTLR, 셀레늄 등의 보조 빌드 도구. 당신이 당신의 빌드 도구, 또는 그와 같은 도구를 사이에 최대의 통합을 하려는 경우 "전체 수명주기"활동이 당신이나 당신의 조직에 중요하다, 당신은 당신의 Clojure의 프로젝트 메이븐을 신중히 고려해야 한다.
다음은 메이븐에서 간단한 Clojure의 프로젝트를 시작하는 기본 pom.xml 파일은 다음과 같습니다.
> Example 8-3. Basic pom.xml suitable for simple Clojure projects  

> ＜project xmlns="http://maven.apache.org/POM/4.0.0"  
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0  
http://maven.apache.org/maven-v4_0_0.xsd"＞  
＜modelVersion＞4.0.0＜/modelVersion＞  
＜groupId＞com.clojurebook＜/groupId＞  
＜artifactId＞sample-maven-project＜/artifactId＞  
＜version＞1.0.0＜/version＞  
＜packaging＞clojure＜/packaging＞  
＜dependencies＞  
＜dependency＞  
＜groupId＞org.clojure＜/groupId＞  
＜artifactId＞clojure＜/artifactId＞  
＜version＞1.3.0＜/version＞  
＜/dependency＞  
＜/dependencies＞  
＜build＞  
＜resources＞  
＜resource＞  
＜directory＞src/main/clojure＜/directory＞  
＜/resource＞  
＜/resources＞  
＜plugins＞  
＜plugin＞  
＜groupId>com.theoryinpractise＜/groupId＞  
＜artifactId>clojure-maven-plugin＜/artifactId＞  
＜version＞1.3.8＜/version＞  
＜extensions＞true＜/extensions＞  
＜configuration＞  
＜warnOnReflection＞true＜/warnOnReflection＞  
＜temporaryOutputDirectory＞true＜/temporaryOutputDirectory＞  
＜/configuration＞  
＜/plugin＞  
＜/plugins＞  
＜/build＞  
＜/project＞

이 _pom.xml_ 은 아래와 같음:

* 메이븐 좌표 **com.clojurebook:sample-maven-project:1.0.0**을 정의.
* 해당 메이븐 생명주기 단계에 해당 AOT 컴파일 및 단위 테스트 목표를 추가 할 **clojure-maven-plugin**을 묻는 **clojure** 패키지 정의
* Clojure v1.3.0(**org.clojure:clojure:1.3.0**)의 단일 의존성 설정.
* 메이븐 자원 디렉토리로서 표준 Clojure 소스 디렉토리가 추가; 이 디렉토리의 Clojure의 소스 파일이 프로젝트의 빌드에 의해 생성된 _.jar_ 파일에 추가됩니다.
* 반사 상호 운용성 통화가 발생하는 경우 경고 및 임시 디렉토리에 생성 된 클래스 파일을 증착에 349 페이지, "AOT 컴파일 구성"에 설명 된 "무결성 체크"모드에서 AOT 컴파일러를 실행 **clojure-maven-plugin**을 구성합니다(패키지 산출물에 포함되지 않습니다.)
_pom.xml_ 을 사용하는 프로젝트의 일반적인 워크플로우는 아래와 같다:
* **mvn Clojure:repl** 프로젝트의 모든 전이 종속성을 포함하도록 설정 클래스 경로에 새로운 Clojure의 REPL을 시작합니다.
* **mvn package** 는 전성 검사와 Clojure의 소스 파일을 AOT 컴파일하고 해당 파일이 들어있는 _.jar_ 파일을 구축 할 것입니다.
* **mvn test** 프로젝트에 존재하는 자바와 Clojure의 모든 테스트를 실행합니다 (각각, _test/src/java_, _test/src/clojure_ 가 최상위임)
* ** mvn install**  -- **mvn package**가 하는-로컬 메이븐 저장소에 _.jar_ 파일을 _설치_합니다.
* **mvn deploy** -- **mvn install**가 하는- pom.xml에 추가를 원하는 원격 저장소에 _.jar_ 파일을 배포한다.

메이븐에서 Clojure의 고유의 모든 기능이 **Clojure-maven-plugin**에 의해 제공되고, Clojure에서의 AOT 컴파일을 호출 제어 옵션을, 프로젝트의 특정 Clojure의 스크립트를 실행, Clojure의 유닛 및 기능 테스트 및 실행을 제공한다. 개발 과정에 유용한 **Clojure:repl**의 많은 목표를 제공한다.
이외에도 **Clojure-Maven-plugin**에서, 다양한 빌드, 테스트 및 프로젝트 관리 작업을 자동화하는 데 도움뿐만 아니라 외부 도구와 환경과의 메이븐 프로젝트의 향상된 통합을 가능하게 할 수 메이븐 플러그인이 많이 있다.
일반적으로, 단순히 **foo Maven** 플러그인에 대한 웹 검색, 당신은 당신이 관심있는 **foo**를 위한 유용한 플러그인을 찾을 가능성이 높다.

### Leiningen

Leiningen 은 "Clojure 빌드 도구는 최신으로 설정하도록 설계하지 않는다."로 다뤄진다. 이 임무는 메이븐과 Ant는 Clojure의 프로젝트의 가장 일반적인 빌드 요구 사항에 대한 의미 복잡성의 일부 좌절에서 크게 부담했다.
그것은 Ant 또는 메이븐보다 일반적인 작업에 대한 간단한 전체 워크 플로우를 제공하는 것을 목표로 하고 있다.
당신 루비, 파이썬, 또는 유사한에서 언어로 부터 JVM 세계 외부에서 오는 경우, 당신은 메이븐 보다 Leiningen에 더 매력을 찾을 수 있다. 이 내부적으로 메이븐의 기능을 재사용하는 동안, Leiningen는 메이븐 의존성 모델의 더 Clojure-관용적 처리 및 경량화 개발 과정을 제공합니다. 특히, Leiningen의 빌드 프로세스를 변경하거나 확장 메이븐 플러그인을 구축하는 데 필요한 무엇을 비교, Clojure의 안에 모두 즐거운 경험을 할 수 있습니다. 트레이드 오프는 Leiningen의 설정을 사용할 수 있습니다. 성장을 계속하고사용 가능한 플러그인이 있지만 Leiningen은 메이븐 환경에서 타사 플러그인의 목록 접근과 통합 옵션을 허용하지 않는다.
여기 라이닝겐에서 간단한 클로져 프로젝트를 시작할 수 있는 기본 _project.clj_ 가 있습니다:
> Example 8-4. Basic project.clj suitable for simple Clojure projects  
(defproject com.clojurebook/sample-lein-project "1.0.0"  
:dependencies [[org.clojure/clojure "1.3.0"]])

**defproject**는 Leiningen에 적합한 프로젝트의 모델을 정의하는 매크로입니다. 이외에도 프로젝트 구성, 대부분을 구성하는 키 - 값 쌍에서 프로젝트의 좌표 (여기 Clojure의 기호와 문자열, **com.clojurebook/sample-lein-project "1.0.0" :** 은 메이븐 표기법 **com.clojurebook:sample-lein-project:1.0.0**에 해당한다.).
*defproject*는 unnamespaced 기호가 같은 값으로 그룹과 이슈 ID를 설정할 수 있는 바로 가기를 제공합니다. 이것은 대부분 프로젝트가 _조직인_ 오픈 소스 프로젝트로 수행됩니다; 예를 들어, ring (Clojure의 웹 프레임 워크)은 **defproject**에 최초의 두 인수로 **ring "1.0.1"**를 갖고 있으며, 메이븐의 결과로 **ring:ring:1.0.1**으로 위치된다.
***
라이닝겐은 새 프로젝트의 스캐폴딩을 만드는 명령을 제공한다. **new my-project**를 호출하는 것은 _project.clj_ 파일을 포함하는 디렉터리를 만들고(*my-project "1.0.0-SNAPSHOT"*의 위치), 소스파일들의 위치를 표시하고 테스트한다.
***
기본 _project.clj_는 샘플 _pom.xml_과 같이 같은 설정을 제공한다(“Maven” on page 345). 루트 클로저 소스 디렉터리는 _src_ 대신 _src/main/clojure_이고, 루트 클로저 소스 테스트 디렉터리는 _test_ 대신 _src/test/clojure_ 이다. 같은 간단한 프로젝트에 대한 두 가지 방법 사이의 진정한 차이는 Leiningen 빌드의 동작은 메이븐 빌드가 각 단계를 구축하기 위한 일관된 순서와 의미를 적용하는 반면 한 때, 명령이 발행하는 순서대로 lein 의존한다는 것입니다.
반면, 기본 Leiningen 워크 플로우는 메이븐과 매우 유사하다:
* **lein repl**은 이전 의존성의 모든 것을 포함하도록 포함 된 classpath로 새로운 Clojure REPL을 프로젝트를 시작한다.
* **lein test**는 프로젝트에서 클로저 테스트의 모든 것을 실행시킨다.(보통 테스트 디렉터리 내).
* **lein jar**는 **mvn package** 처럼 패키징 하지만, 클로저 소스의 AOT 컴파일을을 자동으로 하지는 않는다.
* **lein uberjar**는 **lein jar**에 의한 것처럼 _uberjar_, _.jar_ 파일을 만들지만, 프로젝트의 전이 의존성은 "언팩"되어 들어간다.
* Uberjars들은 간단한 배포를 위해 모든 응응프로그램이 하나의 파일로 배포되거나 단일 자바 호출에 의해 실행되는 곳에서 주로 사용된다.
* **lein compile** _project.clj_의 **:aot** 설정에 기반한 클로저 소스를 AOT 컴파일한다. AOT 컴파일을 위한 Leiningen 설정은, 다음 349페이지의 “AOT compilation configuration”에서 언급한다.
* **lein pom**은 Maven에 호환되는 프로젝트의 _project.clj_파일의 프로젝트와 의존성 정보를 포함한 _pom.xml_ 파일을 만든다.
* **lein deps**는 사용 가능한 프로젝트 명세와 필요하다면 다운로드할 수 있도록 보장한다. 이것은 일반적으로 자동적으로 수행된다.(예를 들어, **:dependencies** 벡터가 변경되어도)
***
당신이 볼 수 있듯이, 표기법은 Leiningen에 종속성을 설명하기 위해 사용 Maven은 문법적으로 매우 다르다. 그러나, 동일한 정보가 전달되고 있다. 이것은 Leiningen 의존성이다.
> [org.clojure/clojure "1.3.0"]

메이븐에서도 정확히 동일하다:

>＜dependency＞  
＜groupId＞org.clojure＜/groupId＞  
＜artifactId＞clojure＜/artifactId＞  
＜version＞1.3.0＜/version＞  
＜/dependency＞  

그것은 더 간결하고, Clojure의 커뮤니티에 확산되어서(예를 들어, 프로젝트 README 파일은 메이븐에서 사용하기에 **<dependency>** XML의 내용보다 Leiningen 스타일의 의존 벡터를 제공 할 가능성이 높다) 우리가 예에서 사용 Clojure의 라이브러리를 참조 할 때마다 책이 시점에서, 우리는 Leiningen의 표기법을 사용하여 해당 의존성을 제공 할 것입니다.
***

![그림. JIT 컴파일러 및 AOT 컴파일러 비교](http://www-01.ibm.com/support/knowledgecenter/api/content/nl/ko/SSSTCZ_3.0.0/com.ibm.wrt.rtlinux.doc.30/realtime/rt2_aot.gif)

### AOT compilation configuration
의존성을 정의하고, 산출물을 패키징하는 것 외에, 클로저 빌드 프로세스에서 당신이 하기 원하는 대부분의 공통된 것의 하나는 클로저 소스들의 AOT 컴파일이다. AOT 컴파일과 관련된 동기와 장단점은 있지만, 계속하기 전에 고려되어야 한다. AOT 컴파일이 프로젝트에 필요하든 원하든 337 페이지의 “Ahead-of-Time Compilation”을 이해하십시오.
어떤 경우에는, Leiningen과 메이븐 모두, 활성화, 비활성화 및 AOT 컴파일을 구성하는 간단한 방법을 제공합니다.
**Leiningen.** 기본적으로 Leiningen 프로젝트의 Clojure의 소스에 AOT 컴파일을 수행하지 않습니다. AOT 슬롯을 해당 슬롯의 값이 될 수있는 프로젝트 구성에 : 그것은 이렇게 구성하면 추가가 필요합니다 :
* **:all**, AOT에 Leiningen하라는 메시지를 표시하는 모두는 프로젝트에 있는 모든 네임 스페이스를 컴파일
* 프로젝트 내에서 네임 스페이스 네임 스페이스를 지정하는 벡터 컴파일.

**:aot** 설정이 추가되었다며, **lein compile**은 AOT 컴파일된 프로젝트의 클로저 네임스페이스를 야기한다.
**Maven.** AOT 컴파일은 기본적으로 **clojure-maven-plugin으로 활성화되고, 적어도, Example 8-3에 보여진 **clojure** 패키징을 사용한다. 그 패키징은 **clojure-maven-plugin**의 컴패일 목표에 할당한다. Maven 컴파일 상태, 그래서 그 AOT 컴파일은 메이븐의 기본 자바 컴파일 종료 후에 실행된다. 만일 클로저 패키징을 사용하지 않으면, 암묵적으로 이런 상태를 설정할 필요가 있다:

>＜plugin＞  
＜groupId＞com.theoryinpractise＜/groupId＞  
＜artifactId＞clojure-maven-plugin＜/artifactId＞  
＜version＞1.3.8＜/version＞  
＜configuration＞  
＜warnOnReflection＞true＜/warnOnReflection＞  
＜temporaryOutputDirectory＞false＜/temporaryOutputDirectory＞  
＜/configuration＞  
＜executions＞  
＜execution＞  
＜id＞compile-clojure＜/id＞  
＜phase>compile＜/phase＞  
＜goals＞  
＜goal＞compile＜/goal＞  
＜/goals＞  
＜/execution＞  
＜/executions＞  
＜/plugin＞

여기 우리는 또한 AOT 컴파일과 밀접한 관계가 있는 두개의 설정 파라미터를 볼 수 있다.: **warnOnReflection** 과 **temporaryOutputDirectory**.
각각이 **warn-on-reflection** 가 활성화되어 있는지 여부를 제어하고, AOT 컴파일에 의해 생성된 클래스 파일이 기본 _클래스_ 출력 디렉터리에 저장되어 있는지, 또는 임시 디렉토리로 이동한다.
**AOT compilation as a sanity check.** 당신은 AOT의 빌드 과정에서 컴파일 (및 클래스 파일 결과를 무시)을 포함 AOTcompiled 클래스 파일을 배포하지 않으려는 경우에도 Clojure의 프로젝트의 코드에 유용한 전성 검사 할 수 있습니다.
AOT 컴파일은 문제의 코드는, 네임 스페이스를 모두 참조 라이브러리를 로드 할 수, vars을, 그리고 자바 클래스가 해결 및 로드해야합니다; 이러한 참조의 어떤 문제가 있는 경우, AOT 컴파일 단계를 발견 할 것이다.
**clojure-maven-plugin**은 이 시나리오를 제공한다; 플러그인의 **<configuration>** 엘레멘트로  **<temporaryOutputDirectory>true</temporaryOutputDirectory>**를 추가하면, AOT 컴파일은 임시 디렉터리로 직접 출력된다. (나중에 삭제됨). 이 AOT의 전성 검사가 수행되는 것을 보장하지만, 그 결과는 여러분의 패키지 유통에 누출되지 않습니다. Leiningen 현재 AOT 컴파일을 수행하지만, 프로젝트의 산출물 (들)을 패키징할 때 생성 된 클래스 파일을 무시하는 쉬운 방법을 제공하지 않습니다. 현재, 가장 좋은 옵션은 **lein compile**을 호출하고,(AOT 컴파일을 수행하는 것으로) 배포를 위한 프로젝트 패키징을 위해 라이닝겐을 이용하기 전에 **lein clean**을 호출는 것이다.
유사하게, AOT 컴파일의 과정 중에 ***warn-on-reflection***을 활성화시킬 수 있다. Clojure는 코드에서 발생하는 상호 운용성 반사 또는 인수 유형 불일치의 각 인스턴스에 대한 경고를 출력합니다. 이 주의들을 활성화시키려면, **<warnOnReflection>true</warnOnReflection>** 엘레멘트를 추가한다. **clojure-maven-plugin** 설정이나, 라이닝겐의 _project.clj_ 파일에 :warn-on-reflection true를 추가한다.

### Building mixed-source projects
기존 프로젝트에 Clojure에 추가하고 싶은 경우에, 당신은 하이브리드 프로젝트의 여러 부분을 컴파일하는 순서에 약간의 관심을 지불해야 할 수도 있습니다. 자바 코드는 동일한 프로젝트에서 Clojure의 유형 정의 양식에 의해 생성된 클래스를 참조 할 필요가 있는 경우 아주 간단하게, 당신은 당신의 자바 소스를 컴파일하기 전에 Clojure의 소스를 AOT 컴파일 해야합니다. 귀하의 Clojure 코드 자바 코드에 정의 된 클래스를 참조 할 필요가 있는 경우 마찬가지로, 자바 소스 먼저 컴파일 될 필요가 있을 것이다.
***
자바 코드가 어떤 Clojure의 정의 유형을 참조하지 않고 Clojure의 코드와 상호 운용되는 경우 혼합 소스 프로젝트와 관련이 빌드 프로세스에 대한 우려는 자바 코드가 같은 프로젝트 내에서 Clojure의 코드를 사용하지 않는 경우에 모든 관련, 또는 하지 않을 수 있다.
***
어떤 경우에, 당신은 내 프로젝트 언어 의존성을 반영하는 빌드 프로세스의 컴파일 단계를 주문해야합니다. Leiningen과 Maven 모두 직접 제공한다.
**
**Maven.** **clojure-maven-plugin** 클로저 코드로된 AOT 컴파일 전에 메이븐의 자바 컴파일을 기본 허용한다. 이것은 **process-resources** 같은 **compile**을 우선 실행하는 메이븐의 상태로 실행하는 **clojure-maven-plugin**의 컴파일 목표를 바인딩하여 변경할 수 있다.
<＜plugin＞  
＜groupId＞com.theoryinpractise＜/groupId＞  
＜artifactId＞clojure-maven-plugin＜/artifactId＞  
＜version＞1.3.8＜/version＞  
＜executions＞  
＜execution＞  
＜id＞clojure-compile＜/id＞  
＜phase＞process-resources＜/phase＞  
＜goals＞  
＜goal＞compile＜/goal＞  
＜/goals＞  
＜/execution＞  
＜/executions＞  
＜/plugin＞

**Leiningen.** **:java-source-path defproject** 슬롯을 이용해 자바코드의 위치를 정의한, 라이닝겐은 설정된 AOT 컴파일에 따라 javac를 이용해 자바 코드를 컴파일하도록 초기화 된다.
Leiningen은 이 순서를 반대로 할 수 있지만 통해 프로그램 대신 메이븐과 구성의 변화를 만드는 것을 의미한다. 먼저, **:java-source-path**는 **defproject**에서 정의되지 않은채로 남는다. 대신, 라이닝겐의 초기 컴파일 순서는 효과에서 남긴다. 우리가 하기 원하는 것은 **compile** 태스크의 행위를 대체하여, **javac**는 태스크의 기본 행위가 완료된다. 라이닝겐은 robert-hooke 라이브러리를 동반하는데, _project.clj 파일 내에서 간결한 방법을 제공한다.

> (defproject com.clojurebook/lein-mixed-source "1.0.0"  
:dependencies [[org.clojure/clojure "1.3.0"]]  
:aot :all)  
(require '(leiningen compile javac))  ①  
(add-hook #'leiningen.compile/compile  ②  
(fn [compile project & args]  ③  
(apply compile project args)  ④  
(leiningen.javac/javac (assoc project :java-source-path "srcj"))))  ⑤

① 먼저 주 라이닝겐 네임스페이스를 요청한다.
② 라이닝겐의 **compile** 태스크를 위한 드라이버 함수로 훅을 추가함; 이것은, **robert-hooke**의 **add-hook** 함수가 **#'leiningen.compile/compile 드라이버 함수 var로 액세스를 요구하기 때문이다.
③  훅 함수는 현재 **project** 설정의 **#'leiningen.compile/compile** var가 점유한 기원 **compile** 함수와 함께 제공된다. 그리고 기원 **compile** 함수에 통과된 모든 다른 인자들도 제공된다. **add-hook** 이 리턴되고, 훅 함수는 **compile** 태스크의 구현을 넘어 훅 함수는 완료될 것이다.
④ 먼저, 우리는 기원 **compile** 함수로 위임하고, 일반 Clojure AOT 컴파일을 진행한다.
⑤ 우리의 자바 소스들을 찾은 곳에서 **javac**가 인지한 **:java-source-path**를 위한 값에 **assoc** 하길 원한다. 이것은 권위있는 프로젝트 모델에 보통 포함된다.(_project.clj_ 파일의 상위에서 **defproject** 로 정의된); 프로젝트의 클로저 소스에 컴파일되는 **javac** 를 실행할 의도가 아닌 **compile**을 하지 않도록 유지한다.
여기에서 **lein compile**을 실행하는 것은 훅 함수를 호출하는 것이므로 클로저 AOT 컴파일은 보통 _srcj_ 디렉터리의 위치한 자바코드를 컴파일하는 **javac** 를 이용하여 컴파일된다.
***
프로젝트에서 코드를 구성 하지만, 당신은 절대적으로 소스 의존성의 인터리빙을 피해야한다. 예를 들어, 자바 코드는 모두 동일한 프로젝트 내의 자바에서 정의한 인터페이스를 구현 Clojure의 정의 형식을 사용 여기서 의존성을 인터리빙할 것이다. 이러한 토폴로지는 (컴파일 목표 실행 임의의 개수와 관련하여 별개의 소스 뿌리의 번호를 사용하는 능력 부여, 다른 곳보다 메이븐 아마도 더욱 용이) 배포될 수 있지만, 그것들은 불량한 디자인의 증상이라고 가정하는 것이 안전하다.
***

# Final Thoughts
효과적으로 코드베이스와 소프트웨어 프로젝트를 조직하는 것은 자신의 도메인입니다. 바라건대 우리는 프로젝트를 구성하고 Clojure의 자체 학습 및 실제 사용한 작업 수행에 초점을 맞출 수 있도록 Clojure 스타일에 내장 될 수 있는 방법에 대해 정진할 수 있도록 제공하고 있습니다.
