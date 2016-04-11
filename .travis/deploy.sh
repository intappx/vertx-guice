if [ ! -z "$TRAVIS_TAG" ]
then
    echo "Deploying release version $TRAVIS_TAG"
    ./gradlew uploadArchives -PnexusUsername="${SONATYPE_USERNAME}" -PnexusPassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${SIGNING_KEY_ID}" -Psigning.password="${SIGNING_PASSWORD}" -Psigning.secretKeyRingFile="local.secring.gpg" -PmavenVersion="${TRAVIS_TAG}"
else
    echo "Deploying snapshot version"
    ./gradlew uploadArchives -PnexusUsername="${SONATYPE_USERNAME}" -PnexusPassword="${SONATYPE_PASSWORD}" -Psigning.keyId="${SIGNING_KEY_ID}" -Psigning.password="${SIGNING_PASSWORD}" -Psigning.secretKeyRingFile="local.secring.gpg"
fi