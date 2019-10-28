JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = \
		World.java \
		Game.java \
		Player.java \
		Bomb.java \
		Explosion.java

MAIN = World

default: classes

run: $(MAIN).class
	$(JVM) $(MAIN)

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class