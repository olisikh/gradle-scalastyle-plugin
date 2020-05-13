/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.alisiikh.scalastyle

import org.gradle.api.GradleException
import org.gradle.workers.WorkAction

import java.lang.reflect.Field
import java.lang.reflect.Method

abstract class ScalastyleCheckAction implements WorkAction<ScalastyleCheckParameters> {
  @Override
  void execute() {
    try {
      Object[] args = [getParameters().getArgs().get() as String[]] as Object[]

      Class<?> codecClass = Thread.currentThread().getContextClassLoader().loadClass('scala.io.Codec$')
      Object codecInstance = codecClass.getField('MODULE$').get(null)
      Field utf8 = codecClass.getDeclaredField('UTF8')
      utf8.setAccessible(true)
      Object codec = utf8.get(codecInstance)
      Class<?> mainClass = Thread.currentThread().getContextClassLoader().loadClass('org.scalastyle.Main$')
      Object mainInstance = mainClass.getField('MODULE$').get(null)
      Method parseArgs = mainClass.getDeclaredMethod('parseArgs', java.lang.String[])
      Object mainConfig = parseArgs.invoke(mainInstance, args)
      Method execute = mainClass.getDeclaredMethods().find { it.getName() == 'execute' }
      execute.setAccessible(true)
      assert execute.invoke(mainInstance, mainConfig, codec) == false
    } catch(AssertionError e) {
      throw new GradleException("Scalastyle check failed.", e)
    } catch(Exception e) {
      throw new RuntimeException(e)
    }
  }
}
