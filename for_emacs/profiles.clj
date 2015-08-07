{:user {
	:plugins [
		[cider/cider-nrepl "0.10.0-SNAPSHOT"]
		[refactor-nrepl "1.2.0-SNAPSHOT"]
	]
	
	:dependencies [
		[slamhound "1.5.5"]
		[org.clojure/tools.nrepl "0.2.10"]
		[acyclic/squiggly-clojure "0.1.3-SNAPSHOT"]
		]
		
	:aliases {
		"slamhound" ["run" "-m" "slam.hound"]
		}
	}
	
}
