def call(source, destination){
    echo "Zipping $source folder to $destination"

    zip dir: "\"$source\"", glob: '', zipFile: "\"$destination\""

    return destination
}
