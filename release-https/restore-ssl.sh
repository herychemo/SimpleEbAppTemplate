
{
  dir2use="${1}"
  S3_BUCKET="gr-general-config"
  MAIN_DOMAIN_ID="sampledocker.grayraccoon.com"
  PREFIX="$(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 15 ;) -"
  echo "${PREFIX} Hi there, performing restore from: ${dir2use}"
  printf "${PREFIX} on: %s \n" "$(date)"
  printf "${PREFIX} dir contains: %s \n\n" "$(ls -m "$dir2use")"

  aws s3 sync "s3://${S3_BUCKET}/ssl-configs/${MAIN_DOMAIN_ID}/" "$dir2use" --delete

  printf "${PREFIX} Now dir contains: %s \n" "$(ls -m "$dir2use")"
} >> /var/log/app-jobs.log
