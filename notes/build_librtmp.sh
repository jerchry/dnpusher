#!/bin/bash




CPU=arm-linux-androideabi
TOOLCHAIN=$NDK_ROOT/toolchains/$CPU-4.9/prebuilt/linux-x86_64
export XCFLAGS="-isysroot $NDK_ROOT/sysroot -isystem $NDK_ROOT/sysroot/usr/include/arm-linux-androideabi -D__ANDROID_API__=21 -g -DANDROID -ffunction-sections -funwind-tables -fstack-protector-strong -no-canonical-prefixes -march=armv7-a -mfloat-abi=softfp -mfpu=vfpv3-d16 -mthumb -Wa,--noexecstack -Wformat -Werror=format-security  -O0 -fPIC"
export XLDFLAGS="--sysroot=${NDK_ROOT}/platforms/android-21/arch-arm "
export CROSS_COMPILE=$TOOLCHAIN/bin/arm-linux-androideabi-


make install SYS=android prefix=`pwd`/install CRYPTO= SHARED=  XDEF=-DNO_SSL 


