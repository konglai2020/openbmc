# This recipe creates a module for the initramfs-framework in OE-core
# which initializes IMA by loading a policy before transferring
# control to the init process in the rootfs. The advantage over having
# that init process doing the policy loading (which systemd could do)
# is that already the integrity of the init binary itself will be
# checked by the kernel.

SUMMARY = "IMA module for the modular initramfs system"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# This policy file will get installed as /etc/ima/ima-policy.
# It is located via the normal file search path, so a .bbappend
# to this recipe can just point towards one of its own files.
IMA_POLICY ?= "ima-policy-hashed"

SRC_URI = " file://ima"

inherit features_check
REQUIRED_DISTRO_FEATURES = "ima"

do_install () {
    install -d ${D}/${sysconfdir}/ima
    install -d ${D}/init.d
    install ${WORKDIR}/ima  ${D}/init.d/20-ima
}

FILES_${PN} = "/init.d ${sysconfdir}"

RDEPENDS_${PN} = "keyutils ${IMA_POLICY}"
RDEPENDS_${PN} += "initramfs-framework-base"
