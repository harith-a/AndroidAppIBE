# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := gmp-prebuilt
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libgmp.so
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := ssl-prebuilt
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libssl_1_0_0.so
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := crypto-prebuilt
LOCAL_SRC_FILES := $(TARGET_ARCH_ABI)/libcrypto_1_0_0.so
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/include
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := ibe
LOCAL_SRC_FILES := 	myencrypt.c\
					ibe_wrap.c\
					config.c\
					ibe_lib.c\
					gen.c\
					crypto.c\
					format.c\
					byte_string.c\
					curve.c\
					fp2.c
LOCAL_SHARED_LIBRARIES := gmp-prebuilt \
							ssl-prebuilt \
							crypto-prebuilt
LOCAL_LDLIBS    += -landroid \
					-llog
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include
include $(BUILD_SHARED_LIBRARY)


