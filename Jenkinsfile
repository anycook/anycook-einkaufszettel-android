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
    stage ('Checkout') {
        checkout scm
    }

    stage ('Setup Environment') {
        env.ANDROID_HOME = "${env.HOME}/android-sdk-linux"
    }

    stage ('Test') {
        sh './gradlew clean testCiUnitTest'
    }

    stage ('Build') {
        try {
            sh './gradlew checkstyle lintCi'
        } finally {
            step([$class: 'LintPublisher'])
            step(
                    [$class                     : 'CheckStylePublisher', pattern: 'app/build/reports/checkstyle/checkstyle.xml',
                     usePreviousBuildAsReference: true])
        }
    }
}

