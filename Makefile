main_class = com/wusisu/learn/annotation/MainTest
compile : src
	mkdir -p build/classes
	javac -d build/classes -sourcepath src src/$(main_class).java
jar : compile
	mkdir -p build/libs
	jar -cvef $(main_class) build/libs/bean.jar -C build/classes .
run : compile
	java -cp build/classes com.wusisu.learn.annotation.MainTest
.PHONY : clean
clean :
	rm -rf build
