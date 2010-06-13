
all:
	find -L . -name "*.java" > srcfiles
	javac -nowarn -d ./classes -source 1.6 -classpath .:./lib/J3DScene.jar:./classes @srcfiles
	rm srcfiles

clean: 
	rm -rf ./classes/*

run:
	java -Djava.library.path=/usr/lib/jni -cp ./classes edu.allatom.PDBParser
