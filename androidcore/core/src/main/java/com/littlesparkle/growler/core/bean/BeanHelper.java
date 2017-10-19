package com.littlesparkle.growler.core.bean;

import android.content.Context;
import android.util.Log;

import com.littlesparkle.growler.core.utility.PrefUtility;

import java.lang.reflect.Field;

/*
 * Copyright (C) 2016-2016, The Little-Sparkle Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS-IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class BeanHelper {
    public static void persist(Context context, Bean bean) {
        Class<?> aClass = bean.getClass();
        do {
            Field[] fields = aClass.getDeclaredFields();
            saveClassNature(context, bean, fields);
            aClass = aClass.getSuperclass();
        } while (aClass != Bean.class);
    }

    public static void load(Context context, Bean bean) {
        Class<?> aClass = bean.getClass();
        do {
            Field[] fields = aClass.getDeclaredFields();
            loadClassNature(context, bean, fields);
            aClass = aClass.getSuperclass();
        } while (aClass != Bean.class);
    }

    public static void update(Context context, Bean bean) {
        Class<?> aClass = bean.getClass();
        do {
            Field[] fields = aClass.getDeclaredFields();
            updateClass(context, bean, fields);
            aClass = aClass.getSuperclass();
        } while (aClass != Bean.class);
    }

    private static void updateClass(Context context, Bean bean, Field[] fields) {
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].get(bean) != null) {
                    if (fields[i].getType() == String.class) {
                        PrefUtility.setString(context, fields[i].getName(), (String) fields[i].get(bean));
                    } else if (fields[i].getType() == int.class || fields[i].getType() == Integer.class) {
                        PrefUtility.setInteger(context, fields[i].getName(), (int) fields[i].get(bean));
                    } else if (fields[i].getType() == double.class || fields[i].getType() == Double.class) {
                        PrefUtility.setDouble(context, fields[i].getName(), (double) fields[i].get(bean));
                    } else if (fields[i].getType() == long.class || fields[i].getType() == Long.class) {
                        PrefUtility.setLong(context, fields[i].getName(), (long) fields[i].get(bean));
                    } else if (fields[i].getType() == boolean.class || fields[i].getType() == Boolean.class) {
                        PrefUtility.setBoolean(context, fields[i].getName(), (boolean) fields[i].get(bean));
                    } else if (fields[i].getType() == float.class || fields[i].getType() == Float.class) {
                        PrefUtility.setFloat(context, fields[i].getName(), (float) fields[i].get(bean));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveClassNature(Context context, Bean bean, Field[] fields) {
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getType() == String.class) {
                    PrefUtility.setString(context, fields[i].getName(), (String) fields[i].get(bean));
                } else if (fields[i].getType() == int.class || fields[i].getType() == Integer.class) {
                    PrefUtility.setInteger(context, fields[i].getName(), (int) fields[i].get(bean));
                } else if (fields[i].getType() == double.class || fields[i].getType() == Double.class) {
                    PrefUtility.setDouble(context, fields[i].getName(), (double) fields[i].get(bean));
                } else if (fields[i].getType() == long.class || fields[i].getType() == Long.class) {
                    PrefUtility.setLong(context, fields[i].getName(), (long) fields[i].get(bean));
                } else if (fields[i].getType() == boolean.class || fields[i].getType() == Boolean.class) {
                    PrefUtility.setBoolean(context, fields[i].getName(), (boolean) fields[i].get(bean));
                } else if (fields[i].getType() == float.class || fields[i].getType() == Float.class) {
                    PrefUtility.setFloat(context, fields[i].getName(), (float) fields[i].get(bean));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadClassNature(Context context, Bean bean, Field[] fields) {
        for (int i = 0; i < fields.length; i++) {
            try {
                if (("serialVersionUID").equals(fields[i].getName())) {
                    continue;
                }
                if (fields[i].getType() == String.class) {
                    fields[i].set(bean, PrefUtility.getString(context, fields[i].getName()));
                } else if (fields[i].getType() == int.class || fields[i].getType() == Integer.class) {
                    fields[i].set(bean, PrefUtility.getInteger(context, fields[i].getName()));
                } else if (fields[i].getType() == double.class || fields[i].getType() == Double.class) {
                    fields[i].set(bean, PrefUtility.getDouble(context, fields[i].getName(), 0));
                } else if (fields[i].getType() == long.class || fields[i].getType() == Long.class) {
                    fields[i].set(bean, PrefUtility.getLong(context, fields[i].getName(), 0));
                } else if (fields[i].getType() == boolean.class || fields[i].getType() == Boolean.class) {
                    fields[i].set(bean, PrefUtility.getBoolean(context, fields[i].getName(), false));
                } else if (fields[i].getType() == float.class || fields[i].getType() == Float.class) {
                    fields[i].set(bean, PrefUtility.getFloat(context, fields[i].getName(), 0));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    public static void dump(Bean bean) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getType() == String.class ||
                        fields[i].getType() == int.class || fields[i].getType() == Integer.class ||
                        fields[i].getType() == double.class || fields[i].getType() == Double.class ||
                        fields[i].getType() == long.class || fields[i].getType() == Long.class ||
                        fields[i].getType() == boolean.class || fields[i].getType() == Boolean.class ||
                        fields[i].getType() == float.class || fields[i].getType() == Float.class) {
                    Log.i("dump bean", fields[i].getName() + " = " + fields[i].get(bean));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clean(Context context, Class<Bean> bean) {
        try {
            Field[] fields = bean.newInstance().getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType() == String.class ||
                        fields[i].getType() == int.class || fields[i].getType() == Integer.class ||
                        fields[i].getType() == double.class || fields[i].getType() == Double.class ||
                        fields[i].getType() == long.class || fields[i].getType() == Long.class ||
                        fields[i].getType() == boolean.class || fields[i].getType() == Boolean.class ||
                        fields[i].getType() == float.class || fields[i].getType() == Float.class) {
                    PrefUtility.delete(context, fields[i].getName());
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
