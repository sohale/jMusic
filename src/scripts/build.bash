set -eu

REPOROOT=$(git rev-parse --show-toplevel)
cd $REPOROOT

# failed attempts to convert raw file:
# brew install sox
# brew install lame
# sox -t raw -b 16  -c 2 -r 44100  OUT.raw out.wav
# afconvert -v -d BEI16@44100   OUT.raw -o out.wav -f wav

cd src
find .|grep -e "\.class" |xargs rm
javac -cp ../src/  jMusic/*.java
java jMusic/JMusicMain
#java jMusic/TreeViewer3
exit

docker run --rm -v $(pwd):/jmusic-src -it openjdk bash -c "cd /jmusic-src/ ; javac -cp /jmusic-src/  jMusic/*.java"

docker run --rm -v $(pwd):/jmusic-src -it openjdk bash -c "cd /jmusic-src/ ; java -cp /jmusic-src/  jMusic/JMusicMain"

