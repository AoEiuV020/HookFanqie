package cc.aoeiuv020.hookfanqie;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressWarnings("RedundantThrows")
public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("handleLoadPackage: " + lpparam.processName + ", " + lpparam.packageName);
        if (Objects.equals(lpparam.processName, lpparam.packageName)) {
            hookDebug(lpparam);
            hookLog(lpparam);
            hookVip(lpparam);
            hookKillAd(lpparam);
            hookUpdate(lpparam);
            hookLuckyDog(lpparam);
        }
    }

    private void hookLog(XC_LoadPackage.LoadPackageParam lpparam) {
        var logLevel = Integer.parseInt(BuildConfig.logLevel);
        XposedHelpers.findAndHookMethod("com.dragon.read.base.util.LogWrapper", lpparam.classLoader, "setLogLevel", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log(param);
                param.args[0] = logLevel;
            }
        });
        XposedHelpers.findAndHookMethod("com.dragon.read.base.util.LogWrapper", lpparam.classLoader, "printLog", "java.lang.String", int.class, "java.lang.String", "java.lang.String", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String module = (String) param.args[0];
                int level = (int) param.args[1];
                if (level < logLevel) {
                    return;
                }
                String tag = (String) param.args[2];
                String msg = (String) param.args[3];
                // 打印日志到xposed，原日志不处理，
                String log = String.format("[%s][%s] %s", module, tag, msg);
                XposedBridge.log(log);
                param.setResult(null);
            }
        });
    }

    private void hookLuckyDog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.dragon.read.polaris.g", lpparam.classLoader, "b", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

    private void hookKillAd(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.dragon.read.base.ad.a", lpparam.classLoader, "a", "java.lang.String", "java.lang.String", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0].toString().contains("_ad")) {
                    param.setResult(false);
                }
            }
        });
        var clazz = XposedHelpers.findClass("com.dragon.read.component.biz.impl.i.f", lpparam.classLoader);
        String[] methods = {
                "isVip",
                "hasNoAdFollAllScene",
                "isAnyVip",
                "hasNoAdPrivilege",
                "adVipAvailable",
        };
        for (String method : methods) {
            XposedHelpers.findAndHookMethod(clazz, method, returnTrue);
        }
        XposedHelpers.findAndHookMethod(clazz, "a", "com.dragon.read.rpc.model.VipSubType", returnTrue);
        XposedHelpers.findAndHookMethod(clazz, "isNoAd", "java.lang.String", returnTrue);
        XposedHelpers.findAndHookMethod(clazz, "canShowVipRelational", returnFalse);
    }

    private void hookUpdate(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.ss.android.update.ad", lpparam.classLoader, "k", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

    private void hookVip(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookConstructor("com.dragon.read.user.model.VipInfoModel", lpparam.classLoader, "java.lang.String", "java.lang.String", "java.lang.String", boolean.class, boolean.class, int.class, boolean.class, "com.dragon.read.rpc.model.VipSubType", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Object[] args = param.args;
                args[0] = "4102415999"; // expireTime
                args[1] = "1"; // isVip
                args[2] = "10000"; // leftTime
                args[3] = true; // isAutoCharge
                args[4] = true; // isUnionVip
                args[5] = 1; // union_source
                args[6] = true; // isAdVip
                args[7] = 0; // vipSubType
            }
        });
    }

    private void log(XC_MethodHook.MethodHookParam param) {
        XposedBridge.log("hook: " + param.method.getDeclaringClass().getName() + "." + param.method.getName());
        if (Objects.equals(BuildConfig.logStackTrace, "true")) {
            XposedBridge.log(new Throwable());
        }
    }

    private void hookDebug(XC_LoadPackage.LoadPackageParam lpparam) {
        var r = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // 无功能，必要时断点使用的，
                log(param);
            }
        };

        if (Objects.equals(BuildConfig.hookResume, "true")) {
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "onResume", r);
        }
        if (Objects.equals(BuildConfig.hookStartActivity, "true")) {
            XposedHelpers.findAndHookMethod("android.app.Activity", lpparam.classLoader, "startActivity", Intent.class, Bundle.class, r);
        }
    }

    @SuppressWarnings("unused")
    private void nothing(XC_LoadPackage.LoadPackageParam lpparam, String clazz, String... methods) {
        var r = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                log(param);
                param.setResult(null);
            }
        };
        for (String method : methods) {
            try {
                XposedHelpers.findAndHookMethod(
                        clazz,
                        lpparam.classLoader, method, r
                );
            } catch (Throwable t) {
                XposedBridge.log(t);
            }
        }
    }
    XC_MethodHook returnTrue = new XC_MethodReplacement() {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            log(param);
            return true;
        }
    };
    XC_MethodHook returnFalse = new XC_MethodReplacement() {
        @Override
        protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
            log(param);
            return false;
        }
    };
}
