import net.poundex.releaseman.util.DirtyObjectGuardian

// Place your Spring DSL code here
beans = {
	importBeans("classpath:/net/poundex/releaseman/release-flow.xml")

	dirtyObjectGuardian(DirtyObjectGuardian)
}
