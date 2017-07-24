def call() {
  def partMap = [:]
  def tokens = env.JOB_NAME.tokenize("/")
  partMap['organization'] = tokens[0]
  partMap['repo'] = tokens[1]
  partMap['branch'] = tokens[2]
  return partMap
}
