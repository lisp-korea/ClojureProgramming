Clojure Programming Study Project
=======================================


Description
----------
[Lisp을 좋아하는 사람들의 그룹][lispkorea]에서 진행중인 [Clojure Programming][clojurebook] 스터디의 연습문제 풀이를 공유하기 위한 저장소입니다.

Requirements
-----------
 * Clojure Programming(Oreilly) 교재
 * 실습에 사용할 플랫폼(공식적으론 [Clojure])
 
How to
------
1. [github][github] 가입
    * 첫 화면에서 아래 Plans, Pricing and Signup 버튼 클릭
    * Plan & Pricing 화면에서 $0/mo Free for open source 란 Create a free account 버튼 클릭
    * user name, email, password를 등록합니다. 
2. [lispkorea-ClojurePrgramming-repo] 접근권한 요청
    * 스터디에 참석해서 요청 하거나 게시판에 등록한 github user name을 올려주세요.
    * 지난시간에 못 나오셨지만 참여를 희망하시는 분은 일단 fork 하셔서 작업하시고 pull request 하시기 바랍니다.

3. SSH Public Key 등록
    * 각 OS별 public key를 생성하세요
    * [Help](http://help.github.com/) 에 가서 "Generating SSH Keys" 항목을 참고하세요
    * 생성된 key를 가신의 계정 Account Settings의 SSH Public Keys ->
       Add another public key에 등록합니다. 
4. lisp-korea/ClojureProgramming [repo][lispkorea-ClojureProgramming-repo] 가져오기

        $ git clone https://github.com/lisp-korea/ClojureProgramming.git

5. git user 설정

        $ git config --global user.name "user name" (github등록한 거요)
        $ git config --global user.email "email 주소"
	
6. 개인 소스 올리기

        $ cd ch01
        practice-<id>.lisp or practice-<id>.clj 식으로 naming으로 소스 생성(문제가 없고 연습개념이라서 각자가 구분될 수 있는 정도면 될 것 같습니다.)
        $ git add <filename>
        $ git commit -m "ex 1.1 by 누구누구"
        $ git push origin master
    

[clojurebook]: http://www.clojurebook.com/
[lispkorea]: http://groups.google.com/group/lisp-korea
[Clojure]: http://en.wikipedia.org/wiki/Common_Lisp
[github]:http://github.com
[lispkorea-onlisp-repo]:http://github.com/lisp-korea/onlisp
