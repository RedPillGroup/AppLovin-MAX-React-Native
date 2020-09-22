require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-applovin-max"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => "9.0" }
  s.source       = { :git => "https://github.com/AppLovin/AppLovin-MAX-React-Native.git", :tag => "#{s.version}" }
  
  s.source_files  = "ios/AppLovinMAX.{h,m}"

  s.dependency "React"
  s.dependency 'AppLovinSDK'
end