package org.openjdk.btrace.instr;

import org.openjdk.btrace.core.extensions.ExtensionEntry;
import org.openjdk.btrace.core.extensions.ExtensionRepository;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class ExtensionBootstrap {
    public static CallSite bootstrap(MethodHandles.Lookup caller, String name, MethodType type, String extName, String extClassName) throws Exception {
        try {
            ExtensionEntry extension = ExtensionRepository.getInstance().getExtensionById(extName);
            if (extension != null) {
                ClassLoader extClassLoader = extension.getClassLoader(caller.lookupClass().getClassLoader());
                Class<?> extClass = extClassLoader.loadClass(extClassName.replace('/', '.'));
                if (extClass != null) {
                    MethodHandle callHandle = MethodHandles.lookup().findStatic(extClass, name, type);
                    return new ConstantCallSite(callHandle);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

        MethodHandle noopHandle =
                MethodHandles.lookup().findStatic(ExtensionBootstrap.class, "noop", MethodType.methodType(void.class));
        return new ConstantCallSite(MethodHandles.dropArguments(noopHandle, 0, type.parameterArray()));
    }

    public static void noop() {}
}
