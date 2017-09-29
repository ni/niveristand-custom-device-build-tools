import ni.vsbuild.BuildFlow
import ni.vsbuild.BuildInformation

def call(lvVersions, dependencies) {
   return new BuildInformation('veristand', lvVersions, dependencies, BuildFlow.GROOVY)
}
