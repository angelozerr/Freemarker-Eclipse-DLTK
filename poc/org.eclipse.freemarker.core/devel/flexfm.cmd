@echo off
PATH=%PATH%;"C:\Program Files\Java\jdk1.6.0_23\bin"
java -Xmx470000000 -cp jflex1.2.2.jar;. JFlex.Main FMTokenizer.jflex -skel skeleton.sse && del -f FMTokenizer.java~ FMTokenizer~ && copy FMTokenizer.java ..\..\..\..\..\org.eclipse.jst.jsp.core\src\org\eclipse\jst\jsp\core\internal\parser\internal\FMTokenizer.java