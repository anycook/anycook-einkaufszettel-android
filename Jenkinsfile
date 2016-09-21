/*
 * This file is part of anycook Einkaufszettel
 *  Copyright (C) 2016 Jan Graßegger, Claudia Sichting
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see [http://www.gnu.org/licenses/].
 */

node {
    stage ('Setup Environment') {
        def workspace = pwd()
        sh 'rm -rf android-sdk-linux'
        sh 'wget -qO- https://dl.google.com/android/android-sdk_r24.4.1-linux.tgz | tar xz'
        sh 'echo y |android-sdk-linux/tools/android update sdk -u --filter tools,platform-tools,build-tools-24.0.2,android-24,extra-android-m2repository'
        env.ANDROID_HOME = "${workspace}/android-sdk-linux"
    }

    stage ('Checkout') {
        checkout scm
    }

    stage ('Test') {
        sh './gradlew clean test'
    }

    stage ('Build') {
        try {
            sh './gradlew build'
        } finally {
            step([$class: 'LintPublisher'])
            step(
                    [$class                     : 'CheckStylePublisher', pattern: 'app/build/reports/checkstyle/checkstyle.xml',
                     usePreviousBuildAsReference: true])
        }
    }
}

