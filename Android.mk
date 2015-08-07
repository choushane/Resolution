LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES += bouncycastle conscrypt telephony-common
LOCAL_STATIC_JAVA_LIBRARIES += android-support-v4 android-support-v13 jsr305

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := \
	$(call all-java-files-under, src) \
#	$(call all-renderscript-files-under, src)

LOCAL_JAVA_LIBRARIES += realtek
LOCAL_CERTIFICATE := platform
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PACKAGE_NAME := TasResolution

LOCAL_AAPT_FLAGS += -c zz_ZZ

include $(BUILD_PACKAGE)
