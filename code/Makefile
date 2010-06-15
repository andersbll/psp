
all:
	find -L . -name "*.java" > srcfiles
	javac -nowarn -d ./classes -source 1.6 -classpath .:./lib/J3DScene.jar:./classes @srcfiles
	rm srcfiles

clean: 
	rm -rf ./classes/*
	rm srcfiles

run:
	java -Djava.library.path=/usr/lib/jni -cp ./classes:/usr/share/java/j3dcore.jar:/usr/share/java/j3dutils.jar:/usr/share/java/vecmath.jar edu.allatom.StructureStats
