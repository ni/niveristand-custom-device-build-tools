import ni.vsbuild.BuildFlow
import ni.vsbuild.BuildInformation

def call(List<String> lvVersions, List<String> dependencies = null) {
   return new BuildInformation('veristand', lvVersions, dependencies, BuildFlow.GROOVY)
}
