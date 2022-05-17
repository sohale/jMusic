set -eu

REPOROOT=$(git rev-parse --show-toplevel)
cd $REPOROOT


cd src
find .|grep -e "\.class" |xargs rm
javac -cp ../src/  jMusic/*.java
java jMusic/JMusicMain
#java jMusic/TreeViewer3
exit

docker run --rm -v $(pwd):/jmusic-src -it openjdk bash -c "cd /jmusic-src/ ; javac -cp /jmusic-src/  jMusic/*.java"

docker run --rm -v $(pwd):/jmusic-src -it openjdk bash -c "cd /jmusic-src/ ; java -cp /jmusic-src/  jMusic/JMusicMain"

