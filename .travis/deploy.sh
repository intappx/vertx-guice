#!/usr/bin/env bash

set -e

SUB_PROJECT=":vertx-guice:"

if [ ! -z "$TRAVIS_TAG" ]; then
    # Release tags in github basically are named with 'v' prefix. Remove this prefix from version component.
    VERSION=${TRAVIS_TAG#v}

    echo "Deploying release version. Tag: ${TRAVIS_TAG}. Version: ${VERSION}"
    ./gradlew ${SUB_PROJECT}uploadArchives -PnexusUsername="${SONATYPE_USERNAME}" -PnexusPassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${SIGNING_KEY_ID}" -Psigning.password="${SIGNING_PASSWORD}" -Psigning.secretKeyRingFile="local.secring.gpg" -PmavenVersion="${VERSION}"
else

    SNAPSHOT_VERSION=$(./gradlew ${SUB_PROJECT}getVersion -q)
    echo "Deploying snapshot version. Version: ${SNAPSHOT_VERSION}"

    ./gradlew ${SUB_PROJECT}uploadArchives -PnexusUsername="${SONATYPE_USERNAME}" -PnexusPassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${SIGNING_KEY_ID}" -Psigning.password="${SIGNING_PASSWORD}" -Psigning.secretKeyRingFile="local.secring.gpg"
fi
