require recipes-devtools/qemu/qemu.inc

QEMU_TARGETS += "aarch64 arm microblaze microblazeel"

SUMMARY = "Xilinx's fork of a fast open source processor emulator"
HOMEPAGE = "https://github.com/xilinx/qemu/"

LIC_FILES_CHKSUM = " \
		file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
		file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913 \
		"

BRANCH ?= ""
REPO ?= "git://github.com/Xilinx/qemu.git;protocol=https"
SRCREV ?= "e8b3d63ceb82432e102147fd627c99f7776dcd10"

BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG}"

S = "${WORKDIR}/git"

# Disable KVM completely
KVMENABLE = "--disable-kvm"

# Strip all appends (needed because qemu.inc adds patches using overrides)
SRC_URI[_append] = ""

DISABLE_STATIC_pn-qemu-xilinx = ""
DISABLE_STATIC_pn-qemu-xilinx-native = ""
DISABLE_STATIC_pn-nativesdk-qemu-xilinx = ""

PTEST_ENABLED = ""

# append a suffix dir, to allow multiple versions of QEMU to be installed
EXTRA_OECONF_append = " \
		--bindir=${bindir}/qemu-xilinx \
		--libexecdir=${libexecdir}/qemu-xilinx \
		--datadir=${datadir}/qemu-xilinx \
		"

do_install() {
	export STRIP="true"
	autotools_do_install

	# Prevent QA warnings about installed ${localstatedir}/run
	if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}

do_deploy() {
	# Create the temp directory for multi-arch QEMU
	install -d ${DEPLOY_DIR_IMAGE}/qemu-tmp
}

addtask deploy after do_install
