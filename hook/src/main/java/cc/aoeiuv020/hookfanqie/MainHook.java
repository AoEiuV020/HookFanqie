package cc.aoeiuv020.hookfanqie;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressWarnings("RedundantThrows")
public class MainHook implements IXposedHookLoadPackage {
    @SuppressWarnings("All")
    private static final boolean DEBUG = BuildConfig.DEBUG && true;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("handleLoadPackage: " + lpparam.processName + ", " + lpparam.packageName);
        XposedHelpers.findAndHookMethod(Instrumentation.class, "callApplicationOnCreate", Application.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (!(param.args[0] instanceof Application)) return;
                hookDebug(lpparam);
                hookVip(lpparam);
                hookKillAd(lpparam);
                hookUpdate(lpparam);
                hookPoplive(lpparam);
                hookLuckyDog(lpparam);
            }
        });

    }

    private void hookLuckyDog(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.dragon.read.polaris.d", lpparam.classLoader, "b", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(false);
            }
        });
    }

    private void hookPoplive(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.dragon.read.component.audio.impl.ui.b.a", lpparam.classLoader, "a",
                Context.class, String.class, String.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(null);
                    }
                });
    }

    private void hookKillAd(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.dragon.read.user.h", lpparam.classLoader, "e", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }

    private void hookUpdate(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookMethod("com.ss.android.update.ad", lpparam.classLoader, "k", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }

    private void hookVip(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedHelpers.findAndHookConstructor("com.dragon.read.user.model.VipInfoModel", lpparam.classLoader,
                String.class, String.class, String.class, boolean.class, boolean.class, int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Object[] args = param.args;
                        String expireTime = (String) args[0];
                        String isVip = (String) args[1];
                        String leftTime = (String) args[2];
                        boolean isAutoCharge = (boolean) args[3];
                        boolean isUnionVip = (boolean) args[4];
                        int union_source = (int) args[5];
                        args[0] = "4102415999";
                        args[1] = "1";
                        args[2] = "10000";
                        args[3] = true;
                        args[4] = true;
                        args[5] = 1;
                    }
                });
    }

    private void log(XC_MethodHook.MethodHookParam param) {
        XposedBridge.log("hook: " + param.thisObject.getClass().getName() + "." + param.method.getName());
        if (DEBUG) {
            XposedBridge.log(new Throwable());
        }
    }

    private void hookDebug(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!DEBUG) {
            return;
        }
        var r = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                // 无功能，必要时断点使用的，
                log(param);
            }
        };

        XposedHelpers.findAndHookMethod(
                "android.app.Activity",
                lpparam.classLoader, "onResume", r
        );
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
}
